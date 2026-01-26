package com.hadiyarajesh.composetemplate.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.hadiyarajesh.composetemplate.navigation.AppNavigation
import com.hadiyarajesh.composetemplate.ui.theme.AppTheme

@Composable
fun ComposeApp() {
    AppTheme {
        val navController = rememberNavController()
        AppNavigation(navController = navController)
    }
}
