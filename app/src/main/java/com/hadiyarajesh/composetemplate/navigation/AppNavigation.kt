package com.hadiyarajesh.composetemplate.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.hadiyarajesh.composetemplate.ui.components.slideBack
import com.hadiyarajesh.composetemplate.ui.components.slideForward
import com.hadiyarajesh.composetemplate.ui.detail.DetailScreenRoute
import com.hadiyarajesh.composetemplate.ui.home.HomeScreenRoute

@Composable
fun AppNavigation(backStack: NavBackStack<NavKey>, modifier: Modifier = Modifier) {
    val popBackStack: () -> Unit = { if (backStack.size > 1) backStack.removeLastOrNull() }
    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = { popBackStack() },
        entryDecorators =
        listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        transitionSpec = { slideForward() },
        popTransitionSpec = { slideBack() },
        predictivePopTransitionSpec = { _ -> slideBack() },
        entryProvider =
        entryProvider {
            entry<NavDestination.Home> {
                HomeScreenRoute(
                    onNavigateToDetail = { image -> backStack.add(NavDestination.Detail(image)) }
                )
            }
            entry<NavDestination.Detail> { key ->
                DetailScreenRoute(
                    image = key.image,
                    onBack = popBackStack
                )
            }
        }
    )
}
