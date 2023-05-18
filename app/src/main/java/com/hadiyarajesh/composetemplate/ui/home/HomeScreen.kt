package com.hadiyarajesh.composetemplate.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.hadiyarajesh.composetemplate.R
import com.hadiyarajesh.composetemplate.ui.components.LoadingIndicator
import com.hadiyarajesh.composetemplate.ui.components.VerticalSpacer
import com.hadiyarajesh.composetemplate.ui.navigation.TopLevelDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeScreenUiState,
    onNavigateClick: (source: String) -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState) {
                is HomeScreenUiState.Loading -> {
                    LoadingIndicator(modifier = Modifier.fillMaxSize())
                }

                is HomeScreenUiState.Success -> {
                    HomeScreenContent(
                        modifier = Modifier.fillMaxSize(),
                        welcomeMessage = uiState.data,
                        onNavigateClick = onNavigateClick
                    )
                }

                is HomeScreenUiState.Error -> {}

                else -> {}
            }
        }
    }
}

@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    welcomeMessage: String,
    onNavigateClick: (source: String) -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = welcomeMessage)
        VerticalSpacer(size = 16)
        Button(onClick = { onNavigateClick(context.getString(R.string.home_screen)) }) {
            Text(
                text = stringResource(
                    R.string.go_to_screen,
                    TopLevelDestination.Detail.javaClass.simpleName
                )
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        uiState = HomeScreenUiState.Success(stringResource(id = R.string.welcome_message)),
        onNavigateClick = {}
    )
}
