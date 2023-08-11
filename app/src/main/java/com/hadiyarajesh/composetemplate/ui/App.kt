package com.hadiyarajesh.composetemplate.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.hadiyarajesh.composetemplate.ui.navigation.AppNavigation
import com.hadiyarajesh.composetemplate.ui.theme.AppTheme

@Composable
fun App() {
    AppTheme {
        val navController = rememberNavController()

        Scaffold { innerPadding ->
            AppNavigation(
                modifier = Modifier.padding(innerPadding),
                navController = navController
            )
        }
    }
}
