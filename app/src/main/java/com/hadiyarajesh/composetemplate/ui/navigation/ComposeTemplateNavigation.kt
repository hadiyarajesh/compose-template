package com.hadiyarajesh.composetemplate.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hadiyarajesh.composetemplate.ui.home.HomeScreen
import com.hadiyarajesh.composetemplate.ui.home.HomeViewModel

@Composable
fun ComposeTemplateNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screens.Home.route
    ) {
        composable(route = Screens.Home.route) {
            val homeViewModel = hiltViewModel<HomeViewModel>()
            HomeScreen(
                navController = navController,
                homeViewModel = homeViewModel
            )
        }
    }
}
