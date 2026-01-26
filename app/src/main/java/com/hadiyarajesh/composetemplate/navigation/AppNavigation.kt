package com.hadiyarajesh.composetemplate.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hadiyarajesh.composetemplate.data.database.entity.Image
import com.hadiyarajesh.composetemplate.ui.components.reverseSlideIntoContainerAnimation
import com.hadiyarajesh.composetemplate.ui.components.reverseSlideOutOfContainerAnimation
import com.hadiyarajesh.composetemplate.ui.components.slideIntoContainerAnimation
import com.hadiyarajesh.composetemplate.ui.components.slideOutOfContainerAnimation
import com.hadiyarajesh.composetemplate.ui.detail.DetailScreenRoute
import com.hadiyarajesh.composetemplate.ui.home.HomeScreenRoute
import com.hadiyarajesh.composetemplate.utility.parcelableType
import kotlin.reflect.typeOf

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = NavDestination.Home
    ) {
        composable<NavDestination.Home>(
            enterTransition = { slideIntoContainerAnimation() },
            exitTransition = { slideOutOfContainerAnimation() }
        ) {
            HomeScreenRoute(navController = navController)
        }

        composable<NavDestination.Detail>(
            typeMap = mapOf(typeOf<Image>() to parcelableType<Image>()),
            enterTransition = { reverseSlideIntoContainerAnimation() },
            exitTransition = { reverseSlideOutOfContainerAnimation() }
        ) { backStackEntry ->
            val detail = backStackEntry.toRoute<NavDestination.Detail>()

            DetailScreenRoute(
                navController = navController,
                image = detail.image
            )
        }
    }
}
