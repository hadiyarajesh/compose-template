# Navigation 3 Migration — Design

- **Date:** 2026-07-20
- **Status:** Approved (pending spec review)
- **Scope:** Replace Jetpack Navigation Compose (Nav2) with Navigation 3 (Nav3) in the Compose template. Faithful migration + predictive-back, single-pane.

## Context / current state

Navigation today (Nav2):

- `navigation/NavDestination.kt` — `sealed interface NavDestination` with `@Serializable` `Home` (`data object`) and `Detail(val image: Image)` (`data class`).
- `navigation/AppNavigation.kt` — `NavHost` + `composable<Home>` / `composable<Detail>`, using `typeMap = mapOf(typeOf<Image>() to parcelableType<Image>())` and per-destination slide `enterTransition`/`exitTransition`.
- `ui/ComposeApp.kt` — owns a `NavController` via `rememberNavController()`.
- `ui/home/HomeScreen.kt` — `HomeScreenRoute(navController, viewModel = hiltViewModel())`; navigates with `navController.navigate(NavDestination.Detail(image))`. Internal `HomeScreenContent` is already stateless and lambda-driven.
- `ui/detail/DetailScreen.kt` — `DetailScreenRoute(navController, image)`; back via `navController.popBackStack()`. No ViewModel.
- `utility/ParcelableType.kt` — custom `NavType<T>` to pass a `@Parcelize` + `@Serializable` object through a Nav2 route.
- `components/AnimationComponents.kt` — slide helpers as extensions on `AnimatedContentTransitionScope<NavBackStackEntry>` (200 ms tween, forward + reverse).
- `data/database/entity/Image.kt` — `@Serializable @Parcelize @Entity data class Image`.

The `parcelableType` machinery exists **only** to pass the `Image` object through Nav2. Nav3's developer-owned back stack removes that need.

## Goals

- Replace Nav2 with Nav3 (`NavDisplay` + a developer-owned back stack).
- Preserve current UX: single-pane, same 200 ms slide transitions.
- Add predictive-back gesture support (Nav3 makes this cheap).
- Keep screens navigation-agnostic and preview-friendly (they receive lambdas, not the back stack).
- Delete now-dead code (`ParcelableType.kt`; `@Parcelize`/parcelize plugin if unused elsewhere).

## Non-goals

- No adaptive/list-detail two-pane scene strategy.
- No change to `Detail`'s data source: it keeps carrying the full `Image` object (decided).
- No change to `HomeViewModel` logic, repository, or Room.

## Key decisions (from brainstorming)

1. **Detail carries the `Image` object** on the back stack (still `@Serializable` for save/restore). Not an id + re-fetch.
2. **Scope:** faithful migration **+ predictive back**. Single-pane.
3. **Back stack ownership (Approach A):** `ComposeApp` holds the back stack; `NavDisplay`'s `entryProvider` passes typed lambdas (`onNavigateToDetail: (Image) -> Unit`, `onBack: () -> Unit`) to routes. No dedicated `AppNavigator` state-holder class (YAGNI for 2 screens).

## Design

### A. Dependencies

`gradle/libs.versions.toml` + `app/build.gradle.kts`:

- **Remove:** `androidx.navigation:navigation-compose` (`navigationCompose` version + `navigation-compose` lib alias).
- **Add:**
  - `androidx.navigation3:navigation3-runtime` — `1.1.4`
  - `androidx.navigation3:navigation3-ui` — `1.1.4`
  - `androidx.lifecycle:lifecycle-viewmodel-navigation3` — per-entry `ViewModelStoreOwner` for `hiltViewModel()`. Version should align with the catalog's `lifecycleRuntimeKtx` (currently `2.11.0`); confirm the exact published coordinate/version at implementation time (the add-on may trail, e.g. `2.10.x`).
- **Keep:** `androidx.hilt:hilt-navigation-compose` — `hiltViewModel()` still originates here and works via the Nav3 ViewModel decorator.

### B. Keys & back stack

- `NavDestination` becomes a Nav3 key: `sealed interface NavDestination : NavKey`. `Home` (`@Serializable data object`) and `Detail(val image: Image)` (`@Serializable data class`) unchanged otherwise.
- `ComposeApp` owns the stack:
  ```kotlin
  val backStack = rememberNavBackStack(NavDestination.Home)
  AppNavDisplay(backStack = backStack)
  ```
  `rememberNavBackStack` returns a snapshot-backed, saveable list of `NavKey`; process-death restore is handled via serialization (hence keys + `Image` must stay `@Serializable`).

### C. NavDisplay (`navigation/AppNavigation.kt`, repurposed)

A composable that takes the `backStack` and renders it:

```kotlin
NavDisplay(
    backStack = backStack,
    onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
    entryDecorators = listOf(
        rememberSavedStateNavEntryDecorator(),      // exact name TBD vs SaveableStateHolder rename
        rememberViewModelStoreNavEntryDecorator(),  // from lifecycle-viewmodel-navigation3
    ),
    transitionSpec = { slideForward() },
    popTransitionSpec = { slideBack() },
    predictivePopTransitionSpec = { slideBack() },
    entryProvider = entryProvider {
        entry<NavDestination.Home> {
            HomeScreenRoute(
                onNavigateToDetail = { image -> backStack.add(NavDestination.Detail(image)) },
            )
        }
        entry<NavDestination.Detail> { key ->
            DetailScreenRoute(
                image = key.image,
                onBack = { backStack.removeLastOrNull() },
            )
        }
    },
)
```

- **Decorators:** `rememberSceneSetupNavEntryDecorator()` is **not** included — it was removed in Nav3 `1.0.0-alpha11` and scene setup is applied by default. Only the saveable-state and viewmodel-store decorators are added. Exact function names to be confirmed against `1.1.4` (the saveable-state decorator was renamed toward `SaveableStateHolderNavEntryDecorator`).
- **Back handling:** `onBack` pops only when more than one entry remains, so `Home` is never popped to a blank screen; at the root, system back exits the app (default).

### D. Animations + predictive back

- Reproduce the current 200 ms slides through `NavDisplay`'s `transitionSpec` (push), `popTransitionSpec` (pop), and `predictivePopTransitionSpec` (back-gesture preview reuses the pop slide).
- Refactor `components/AnimationComponents.kt`: change the extension receiver from `AnimatedContentTransitionScope<NavBackStackEntry>` to `AnimatedContentTransitionScope<*>` (Nav3's transition scope) and expose `ContentTransform`-producing helpers for forward/back. Keep `NAVIGATION_ANIMATION_DURATION = 200`, `EaseIn`/`EaseOut`.

### E. Screen signature changes

- `HomeScreenRoute(onNavigateToDetail: (Image) -> Unit, viewModel: HomeViewModel = hiltViewModel())` — drop the `NavController` param; wire `onNavigateClick` to `onNavigateToDetail`. `hiltViewModel()` unchanged. Internal `HomeScreenContent` unchanged.
- `DetailScreenRoute(image: Image, onBack: () -> Unit)` — drop `NavController`; `onBackClick` → `onBack`. Internal `DetailScreenContent` unchanged.

### F. Cleanups (consequences)

- **Delete `utility/ParcelableType.kt`** — its only purpose (passing `Image` through a Nav2 route) is gone.
- **`Image`:** remove `@Parcelize` + `: Parcelable` (now unused). Keep `@Serializable` + `@Entity`.
- **`kotlin-parcelize` plugin:** remove from `app/build.gradle.kts` plugins and the catalog **iff** no other `@Parcelize` usage remains (grep to confirm; `Image` is the only current user).

### G. Data flow

1. Launch → `ComposeApp` creates `backStack = [Home]` → `NavDisplay` renders the Home entry → `HomeScreenRoute(onNavigateToDetail = { backStack.add(Detail(it)) })`.
2. Tap "go to detail" → `backStack.add(Detail(image))` → push slide → `DetailScreenRoute(image, onBack = { backStack.removeLastOrNull() })`.
3. Back button/gesture → `NavDisplay.onBack` → pop (if size > 1) → pop/predictive slide back to Home.

### H. Testing & verification

- `HomeViewModelTest` untouched (ViewModel logic unaffected). Compose Preview screenshot tests untouched.
- `./gradlew spotlessCheck test` and `:app:compileDebugKotlin` must stay green.
- Manual/app verification: Home→Detail push slide; back button and **back-swipe predictive** return; rotate on Detail (process-death path) restores the same `Image` (serialization saver).

### I. Affected files

| File | Change |
|---|---|
| `gradle/libs.versions.toml` | remove nav2 lib/version; add nav3 runtime/ui + viewmodel-nav3 |
| `app/build.gradle.kts` | swap deps; remove `kotlin-parcelize` (if unused) |
| `navigation/NavDestination.kt` | `NavDestination : NavKey` |
| `navigation/AppNavigation.kt` | rewrite as `NavDisplay` + `entryProvider` + decorators + transitions |
| `ui/ComposeApp.kt` | `rememberNavBackStack(Home)`, pass to display |
| `ui/home/HomeScreen.kt` | route takes `onNavigateToDetail` lambda |
| `ui/detail/DetailScreen.kt` | route takes `image` + `onBack` lambda |
| `components/AnimationComponents.kt` | retarget scope to `AnimatedContentTransitionScope<*>` |
| `utility/ParcelableType.kt` | **delete** |
| `data/database/entity/Image.kt` | drop `@Parcelize`/`Parcelable` |

## Risks / verification items

- Exact Nav3 `1.1.4` API surface: decorator function names (saveable-state rename), `entryProvider`/`entry<T>` DSL import paths, `NavDisplay` parameter names — confirm against current docs during planning/implementation.
- `lifecycle-viewmodel-navigation3` coordinate/version and its compatibility with lifecycle `2.11.0`.
- Whether `hilt-navigation-compose` still pulls nav2 (`navigation-compose`) transitively; if so, note it (harmless) or add an exclusion.
