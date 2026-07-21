# Navigation 3 Migration Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Replace Jetpack Navigation Compose (Nav2) with Navigation 3 (Nav3) in the Compose template, keeping current UX and adding predictive-back.

**Architecture:** A developer-owned back stack (`rememberNavBackStack`) lives in `ComposeApp`; `AppNavigation` renders it with `NavDisplay` + an `entryProvider`, passing typed lambdas to each screen (screens stay navigation-agnostic). `Detail` carries the `Image` object directly, eliminating the `parcelableType` NavType.

**Tech Stack:** Kotlin, Jetpack Compose, Navigation 3 (`androidx.navigation3` 1.1.4 + `androidx.lifecycle:lifecycle-viewmodel-navigation3`), Hilt, kotlinx.serialization, Room, Retrofit, ktlint/Spotless.

## Global Constraints

- Navigation 3 runtime + ui pinned at `1.1.4`; `lifecycle-viewmodel-navigation3` version confirmed by build resolution in Task 1 (post-cutoff; do not guess — let Gradle resolve/verify).
- minSdk 23, compileSdk 37, targetSdk 36, JVM 21 (unchanged).
- Keys and `Image` MUST remain `@Serializable` (Nav3 saves the back stack via serialization).
- Screens receive lambdas, never the back stack or a controller (preserve preview-friendly, navigation-agnostic routes).
- After each task: `./gradlew spotlessApply` then the task's verification must be green. Commit at the end of each task.
- Spotless is ratcheted to `origin/master`; run `./gradlew spotlessApply` before committing so changed files pass `spotlessCheck`.
- **API-name caveat:** Nav3 1.1.4 is past the author's knowledge cutoff. Where a step marks `[verify]`, confirm the exact import/symbol against IDE autocomplete or the current docs; the accompanying build step will catch mismatches.

---

### Task 1: Add Navigation 3 dependencies (alongside Nav2)

Add Nav3 to the catalog and module without removing Nav2 yet, so the build stays green while we confirm the artifacts resolve.

**Files:**
- Modify: `gradle/libs.versions.toml`
- Modify: `app/build.gradle.kts`

**Interfaces:**
- Produces: catalog aliases `libs.navigation3.runtime`, `libs.navigation3.ui`, `libs.lifecycle.viewmodel.navigation3` for later tasks.

- [ ] **Step 1: Add version refs to `[versions]` in `gradle/libs.versions.toml`**

Add near `navigationCompose`:

```toml
navigation3 = "1.1.4"
lifecycleViewmodelNavigation3 = "2.10.0-rc01"
```

> `[verify]` `lifecycleViewmodelNavigation3`: this is the last-known value. If Step 5 fails to resolve it, open https://developer.android.com/jetpack/androidx/releases/lifecycle, use the current `lifecycle-viewmodel-navigation3` version, and update this ref.

- [ ] **Step 2: Add library aliases to `[libraries]`**

```toml
navigation3-runtime = { group = "androidx.navigation3", name = "navigation3-runtime", version.ref = "navigation3" }
navigation3-ui = { group = "androidx.navigation3", name = "navigation3-ui", version.ref = "navigation3" }
lifecycle-viewmodel-navigation3 = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-navigation3", version.ref = "lifecycleViewmodelNavigation3" }
```

- [ ] **Step 3: Add the dependencies in `app/build.gradle.kts`**

In the `dependencies { }` block, next to `implementation(libs.navigation.compose)`:

```kotlin
implementation(libs.navigation3.runtime)
implementation(libs.navigation3.ui)
implementation(libs.lifecycle.viewmodel.navigation3)
```

- [ ] **Step 4: Format**

Run: `./gradlew spotlessApply`

- [ ] **Step 5: Verify dependencies resolve and the project still builds**

Run: `./gradlew :app:compileDebugKotlin`
Expected: `BUILD SUCCESSFUL`. If a Nav3 artifact fails to resolve, fix the version refs from Step 1 and re-run.

- [ ] **Step 6: Commit**

```bash
git add gradle/libs.versions.toml app/build.gradle.kts
git commit -m "build: add Navigation 3 dependencies alongside Nav2"
```

---

### Task 2: Migrate navigation to Navigation 3

Atomic swap: this is one deliverable because Nav2 and Nav3 cannot both wire the same screens/animations. Intermediate steps may not compile; the task ends with a green build + running app. Nav2 is removed at the end.

**Files:**
- Modify: `app/src/main/java/com/hadiyarajesh/composetemplate/navigation/NavDestination.kt`
- Modify: `app/src/main/java/com/hadiyarajesh/composetemplate/ui/components/AnimationComponents.kt`
- Modify: `app/src/main/java/com/hadiyarajesh/composetemplate/ui/home/HomeScreen.kt` (route only)
- Modify: `app/src/main/java/com/hadiyarajesh/composetemplate/ui/detail/DetailScreen.kt` (route only)
- Modify: `app/src/main/java/com/hadiyarajesh/composetemplate/navigation/AppNavigation.kt`
- Modify: `app/src/main/java/com/hadiyarajesh/composetemplate/ui/ComposeApp.kt`
- Modify: `gradle/libs.versions.toml`, `app/build.gradle.kts` (remove Nav2)

**Interfaces:**
- Consumes: `libs.navigation3.*` (Task 1).
- Produces:
  - `NavDestination : NavKey` with `Home` and `Detail(val image: Image)`.
  - `AnimatedContentTransitionScope<*>.slideForward(): ContentTransform`, `AnimatedContentTransitionScope<*>.slideBack(): ContentTransform`.
  - `HomeScreenRoute(onNavigateToDetail: (Image) -> Unit, viewModel: HomeViewModel = hiltViewModel())`.
  - `DetailScreenRoute(image: Image, onBack: () -> Unit)`.
  - `AppNavigation(backStack: NavBackStack, modifier: Modifier = Modifier)`.

- [ ] **Step 1: Make `NavDestination` a `NavKey`**

Replace `NavDestination.kt` with:

```kotlin
package com.hadiyarajesh.composetemplate.navigation

import androidx.navigation3.runtime.NavKey
import com.hadiyarajesh.composetemplate.data.database.entity.Image
import kotlinx.serialization.Serializable

/**
 * All navigation destinations in the app. Each is a Nav3 [NavKey] placed on the
 * developer-owned back stack. Keys stay @Serializable so the back stack survives
 * process death.
 */
sealed interface NavDestination : NavKey {
    @Serializable
    data object Home : NavDestination

    @Serializable
    data class Detail(val image: Image) : NavDestination
}
```

> `[verify]` import `androidx.navigation3.runtime.NavKey`.

- [ ] **Step 2: Refactor `AnimationComponents.kt` to Nav3 transition scope**

Replace the file with `ContentTransform`-producing helpers on `AnimatedContentTransitionScope<*>`:

```kotlin
package com.hadiyarajesh.composetemplate.ui.components

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith

const val NAVIGATION_ANIMATION_DURATION = 200

/** Forward (push) transition: new screen slides in from the end edge. */
fun AnimatedContentTransitionScope<*>.slideForward(): ContentTransform =
    slideIntoContainer(
        towards = SlideDirection.Start,
        animationSpec = tween(NAVIGATION_ANIMATION_DURATION, easing = EaseIn),
    ) togetherWith
        slideOutOfContainer(
            towards = SlideDirection.Start,
            animationSpec = tween(NAVIGATION_ANIMATION_DURATION, easing = EaseOut),
        )

/** Back (pop / predictive) transition: previous screen slides back in from the start edge. */
fun AnimatedContentTransitionScope<*>.slideBack(): ContentTransform =
    slideIntoContainer(
        towards = SlideDirection.End,
        animationSpec = tween(NAVIGATION_ANIMATION_DURATION, easing = EaseIn),
    ) togetherWith
        slideOutOfContainer(
            towards = SlideDirection.End,
            animationSpec = tween(NAVIGATION_ANIMATION_DURATION, easing = EaseOut),
        )
```

> The old `AnimatedBackStackEntry` typealias, `slideIntoContainerAnimation`, `slideOutOfContainerAnimation`, `reverseSlide*` helpers, and the `NavBackStackEntry` import are removed. SlideDirection values reproduce standard forward/back motion; adjust if the visual direction feels reversed when you run the app (Step 8).

- [ ] **Step 3: Update `HomeScreenRoute` to take a lambda**

In `HomeScreen.kt`, replace the route composable (leave `HomeScreenContent` and everything below unchanged):

```kotlin
@Composable
internal fun HomeScreenRoute(
    onNavigateToDetail: (Image) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val homeScreenUiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreenContent(
        uiState = homeScreenUiState,
        loadData = { viewModel.loadData() },
        onNavigateClick = onNavigateToDetail,
        onChangeImageClick = { image -> viewModel.changeImage(image) },
    )
}
```

Remove the now-unused imports `androidx.navigation.NavController` and `com.hadiyarajesh.composetemplate.navigation.NavDestination` from this file.

- [ ] **Step 4: Update `DetailScreenRoute` to take image + onBack**

In `DetailScreen.kt`, replace the route composable (leave `DetailScreenContent` and below unchanged):

```kotlin
@Composable
internal fun DetailScreenRoute(
    image: Image,
    onBack: () -> Unit,
) {
    DetailScreenContent(
        image = image,
        onBackClick = onBack,
    )
}
```

Remove the now-unused import `androidx.navigation.NavController` from this file.

- [ ] **Step 5: Rewrite `AppNavigation.kt` as a `NavDisplay`**

Replace the file with:

```kotlin
package com.hadiyarajesh.composetemplate.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.hadiyarajesh.composetemplate.ui.components.slideBack
import com.hadiyarajesh.composetemplate.ui.components.slideForward
import com.hadiyarajesh.composetemplate.ui.detail.DetailScreenRoute
import com.hadiyarajesh.composetemplate.ui.home.HomeScreenRoute

@Composable
fun AppNavigation(
    backStack: NavBackStack,
    modifier: Modifier = Modifier,
) {
    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
        entryDecorators =
            listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
        transitionSpec = { slideForward() },
        popTransitionSpec = { slideBack() },
        predictivePopTransitionSpec = { slideBack() },
        entryProvider =
            entryProvider {
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
}
```

> `[verify]` imports/symbols against 1.1.4: `NavBackStack`, `entry`, `entryProvider`, `NavDisplay`, `rememberSaveableStateHolderNavEntryDecorator` (package `androidx.navigation3.runtime`), `rememberViewModelStoreNavEntryDecorator` (package `androidx.lifecycle.viewmodel.navigation3`). If `NavBackStack` is generic in this version, use `NavBackStack<NavKey>`. If `predictivePopTransitionSpec` takes a swipe-edge parameter, accept and ignore it: `predictivePopTransitionSpec = { _ -> slideBack() }`.

- [ ] **Step 6: Own the back stack in `ComposeApp.kt`**

Replace the file with:

```kotlin
package com.hadiyarajesh.composetemplate.ui

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.rememberNavBackStack
import com.hadiyarajesh.composetemplate.navigation.AppNavigation
import com.hadiyarajesh.composetemplate.navigation.NavDestination
import com.hadiyarajesh.composetemplate.ui.theme.AppTheme

@Composable
fun ComposeApp() {
    AppTheme {
        val backStack = rememberNavBackStack(NavDestination.Home)
        AppNavigation(backStack = backStack)
    }
}
```

> `[verify]` `rememberNavBackStack` from `androidx.navigation3.runtime`. It returns the `NavBackStack` type used in Step 5.

- [ ] **Step 7: Remove the Nav2 dependency**

In `gradle/libs.versions.toml`: delete the `navigationCompose` version ref and the `navigation-compose` library alias.
In `app/build.gradle.kts`: delete `implementation(libs.navigation.compose)`.

- [ ] **Step 8: Format, build, test, and run**

Run: `./gradlew spotlessApply`
Run: `./gradlew spotlessCheck test`
Expected: `BUILD SUCCESSFUL` (unit tests unaffected).
Run: `./gradlew :app:compileDebugKotlin :app:compileDebugScreenshotTestKotlin`
Expected: `BUILD SUCCESSFUL`.
Then install/run the app and verify manually:
- Home → tap "go to detail" → Detail slides in (forward).
- System back and **back-swipe gesture** → Detail slides back to Home (predictive preview during the swipe).
- On Detail, rotate the device → the same image remains (back-stack serialization restore).

> If `hilt-navigation-compose` still pulls `navigation-compose` transitively, that's acceptable (unused). To confirm it's gone from the direct graph: `./gradlew :app:dependencies --configuration debugRuntimeClasspath | grep navigation`.

- [ ] **Step 9: Commit**

```bash
git add app/src gradle/libs.versions.toml app/build.gradle.kts
git commit -m "feat: migrate navigation from Nav2 to Navigation 3"
```

---

### Task 3: Remove dead Parcelable machinery

With the `parcelableType` NavType gone, `Image` no longer needs to be `Parcelable`.

**Files:**
- Delete: `app/src/main/java/com/hadiyarajesh/composetemplate/utility/ParcelableType.kt`
- Modify: `app/src/main/java/com/hadiyarajesh/composetemplate/data/database/entity/Image.kt`
- Modify: `app/build.gradle.kts`, `gradle/libs.versions.toml` (remove `kotlin-parcelize`)

- [ ] **Step 1: Confirm `@Parcelize` is used nowhere else**

Run: `grep -rn "Parcelize\|Parcelable\|parcelableType" app/src`
Expected: matches only in `Image.kt` and `ParcelableType.kt`. If anything else uses `@Parcelize`, stop and keep the plugin.

- [ ] **Step 2: Delete `ParcelableType.kt`**

```bash
git rm app/src/main/java/com/hadiyarajesh/composetemplate/utility/ParcelableType.kt
```

- [ ] **Step 3: Drop `Parcelable` from `Image`**

Replace `Image.kt` with:

```kotlin
package com.hadiyarajesh.composetemplate.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class Image(
    @PrimaryKey(autoGenerate = true)
    val imageId: Long = 0,
    val url: String,
    val description: String,
    val altText: String,
)
```

- [ ] **Step 4: Remove the `kotlin-parcelize` plugin**

In `app/build.gradle.kts`, delete `alias(libs.plugins.kotlin.parcelize)` from the `plugins { }` block.
In `gradle/libs.versions.toml`, delete the `kotlin-parcelize` entry from `[plugins]`.

- [ ] **Step 5: Format, build, test**

Run: `./gradlew spotlessApply`
Run: `./gradlew spotlessCheck test :app:compileDebugKotlin`
Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 6: Commit**

```bash
git add app/src app/build.gradle.kts gradle/libs.versions.toml
git commit -m "refactor: drop Parcelable from Image and remove kotlin-parcelize"
```

---

### Task 4: Final verification & spec sync

- [ ] **Step 1: Full green build**

Run: `./gradlew spotlessCheck test :app:compileDebugKotlin :app:compileDebugScreenshotTestKotlin`
Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 2: Update the spec's Status**

In `docs/superpowers/specs/2026-07-20-navigation3-migration-design.md`, change `Status:` to `Implemented`, and note the resolved values (Nav3 version, `lifecycle-viewmodel-navigation3` version, whether nav2 remained transitive).

- [ ] **Step 3: Commit**

```bash
git add docs/superpowers/specs/2026-07-20-navigation3-migration-design.md
git commit -m "docs: mark Navigation 3 migration spec as implemented"
```

- [ ] **Step 4: (Optional) push & open PR**

```bash
git push -u origin feat/navigation3
gh pr create --base master --head feat/navigation3 --title "feat: migrate to Navigation 3" --body "Implements docs/superpowers/specs/2026-07-20-navigation3-migration-design.md"
```

> Note: `feat/navigation3` is stacked on `chore/modernize-tooling` (PR #27). Either target the PR base at `chore/modernize-tooling`, or rebase onto `master` after #27 merges.

## Self-Review

**Spec coverage:** deps swap (Task 1 + 2.7) ✓; NavKey (2.1) ✓; back stack ownership (2.6) ✓; NavDisplay + entryProvider + decorators + onBack (2.5) ✓; animations + predictive back (2.2, 2.5) ✓; screen signatures (2.3, 2.4) ✓; delete ParcelableType + @Parcelize + parcelize plugin (Task 3) ✓; testing/verification (2.8, 4.1) ✓; risks/verify items handled inline via `[verify]` notes and build gates ✓.

**Placeholder scan:** no "TBD/TODO" for our own logic; the two external-API unknowns (viewmodel-nav3 version, exact decorator/import symbols) are handled by explicit `[verify]` steps backed by build gates, not left vague.

**Type consistency:** `slideForward()`/`slideBack()` defined in 2.2 and used in 2.5; `HomeScreenRoute(onNavigateToDetail)` / `DetailScreenRoute(image, onBack)` defined in 2.3/2.4 and called in 2.5; `NavBackStack` produced by `rememberNavBackStack` (2.6) and consumed by `AppNavigation` (2.5). Consistent.
