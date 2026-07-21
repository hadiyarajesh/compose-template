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
