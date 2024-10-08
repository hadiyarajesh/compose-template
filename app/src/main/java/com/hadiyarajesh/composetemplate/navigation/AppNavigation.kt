package com.hadiyarajesh.composetemplate.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hadiyarajesh.composetemplate.ui.detail.DetailRoute
import com.hadiyarajesh.composetemplate.ui.home.HomeRoute
import com.hadiyarajesh.composetemplate.utility.Constants

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = TopLevelDestination.Home.route
    ) {
        composable(
            route = TopLevelDestination.Home.route,
            enterTransition = {
                slideIntoContainer(
                    animationSpec = tween(
                        durationMillis = Constants.NAVIGATION_ANIMATION_DURATION,
                        easing = EaseIn
                    ),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    animationSpec = tween(
                        durationMillis = Constants.NAVIGATION_ANIMATION_DURATION,
                        easing = EaseOut
                    ),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            }
        ) {
            HomeRoute(
                onNavigateClick = { source ->
                    navController.navigate(TopLevelDestination.Detail.withArgs(source))
                }
            )
        }

        composable(
            route = TopLevelDestination.Detail.route + "/{${Constants.SOURCE}}",
            arguments = listOf(
                navArgument(Constants.SOURCE) {
                    type = NavType.StringType
                }
            ),
            enterTransition = {
                slideIntoContainer(
                    animationSpec = tween(
                        durationMillis = Constants.NAVIGATION_ANIMATION_DURATION,
                        easing = EaseIn
                    ),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    animationSpec = tween(
                        durationMillis = Constants.NAVIGATION_ANIMATION_DURATION,
                        easing = EaseOut
                    ),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) { backStackEntry ->
            val source = backStackEntry.arguments?.getString(Constants.SOURCE) ?: return@composable

            DetailRoute(
                source = source,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
