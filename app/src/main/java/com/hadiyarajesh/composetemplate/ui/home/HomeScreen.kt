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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hadiyarajesh.composetemplate.R
import com.hadiyarajesh.composetemplate.data.entity.Message
import com.hadiyarajesh.composetemplate.navigation.TopLevelDestination
import com.hadiyarajesh.composetemplate.ui.components.ErrorItem
import com.hadiyarajesh.composetemplate.ui.components.LoadingIndicator
import com.hadiyarajesh.composetemplate.ui.components.VerticalSpacer

/**
 * This composable function is used to be called from [AppNavigation].
 */
@Composable
fun HomeRoute(
    onNavigateClick: (source: String) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val homeScreenUiState by remember { homeViewModel.response }.collectAsStateWithLifecycle()

    HomeScreen(
        uiState = homeScreenUiState,
        loadData = { homeViewModel.loadData() },
        onNavigateClick = onNavigateClick
    )
}

/**
 * This composable function is used to display the content of home screen that is preview-able,
 * which means it does not take any dependency that would be difficult to provide from compose preview.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeScreenUiState,
    loadData: () -> Unit,
    onNavigateClick: (source: String) -> Unit
) {
    LaunchedEffect(Unit) {
        loadData()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState) {
                is HomeScreenUiState.Initial -> {}

                is HomeScreenUiState.Loading -> {
                    LoadingIndicator(modifier = Modifier.fillMaxSize())
                }

                is HomeScreenUiState.Success -> {
                    HomeScreenContent(
                        modifier = Modifier.fillMaxSize(),
                        welcomeMessage = uiState.msg.text,
                        onNavigateClick = onNavigateClick
                    )
                }

                is HomeScreenUiState.Error -> {
                    ErrorItem(
                        text = uiState.msg,
                        modifier = Modifier.fillMaxSize()
                    )
                }
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
        Button(
            onClick = {
                onNavigateClick(
                    context.getString(R.string.screen_name).format(TopLevelDestination.Home.title)
                )
            }
        ) {
            Text(
                text = stringResource(
                    R.string.go_to_screen,
                    TopLevelDestination.Detail.title
                )
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        uiState = HomeScreenUiState.Success(msg = Message(text = stringResource(id = R.string.welcome_message))),
        loadData = {},
        onNavigateClick = {}
    )
}
