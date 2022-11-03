package com.hadiyarajesh.composetemplate.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.hadiyarajesh.composetemplate.ui.navigation.ComposeTemplateNavigation
import com.hadiyarajesh.composetemplate.ui.theme.ComposeTemplateTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeTemplateApp() {
    ComposeTemplateTheme {
        val navController = rememberNavController()

        Scaffold { innerPadding ->
            ComposeTemplateNavigation(
                modifier = Modifier.padding(innerPadding),
                navController = navController
            )
        }
    }
}
