package com.hadiyarajesh.composetemplate.ui.home

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import com.hadiyarajesh.composetemplate.R
import com.hadiyarajesh.composetemplate.data.entity.Image
import com.hadiyarajesh.composetemplate.navigation.TopLevelDestination
import com.hadiyarajesh.composetemplate.ui.components.ErrorItem
import com.hadiyarajesh.composetemplate.ui.components.LoadingIndicator
import com.hadiyarajesh.composetemplate.ui.components.TextWithIcon
import com.hadiyarajesh.composetemplate.ui.components.VerticalSpacer
import com.hadiyarajesh.composetemplate.utility.Constants

/**
 * This composable function is used to be called from [com.hadiyarajesh.composetemplate.navigation.AppNavigation].
 */
@Composable
internal fun HomeRoute(
    onNavigateClick: (source: String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val homeScreenUiState by remember { viewModel.uiState }.collectAsStateWithLifecycle()

    HomeScreen(
        uiState = homeScreenUiState,
        loadData = { viewModel.loadData() },
        onNavigateClick = {
            onNavigateClick(
                context.getString(
                    R.string.screen_name,
                    TopLevelDestination.Home.title
                )
            )
        }
    )
}

/**
 * This composable function is used to display the content of home screen that is preview-able,
 * which means it does not take any dependency that would be difficult to provide from compose preview.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    uiState: HomeScreenUiState,
    loadData: () -> Unit,
    onNavigateClick: () -> Unit
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
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (uiState) {
                is HomeScreenUiState.Initial -> {}

                is HomeScreenUiState.Loading -> {
                    LoadingIndicator(modifier = Modifier.fillMaxSize())
                }

                is HomeScreenUiState.Success -> {
                    HomeScreenContent(
                        modifier = Modifier.fillMaxSize(),
                        image = uiState.data,
                        onNavigateClick = onNavigateClick
                    )
                }

                is HomeScreenUiState.Error -> {
                    ErrorItem(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                        text = uiState.msg
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    image: Image,
    onNavigateClick: () -> Unit
) {
    val imageShape = RoundedCornerShape(16.dp)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(300.dp)
                .clip(imageShape)
                .border(1.dp, Color.LightGray, imageShape)
                .shadow(5.dp, imageShape)
        ) {
            SubcomposeAsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = image.url,
                contentDescription = image.description,
                loading = { LoadingIndicator(strokeWidth = 2.dp) },
                error = { ErrorItem(text = image.altText) }
            )
        }
        VerticalSpacer(size = 16)

        Text(
            text = image.description,
            style = MaterialTheme.typography.titleSmall
        )
        VerticalSpacer(size = 16)

        OutlinedButton(onClick = onNavigateClick) {
            TextWithIcon(
                text = stringResource(
                    R.string.go_to_screen,
                    TopLevelDestination.Detail.title
                ),
                icon = Icons.AutoMirrored.Filled.ArrowForward

            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        uiState = HomeScreenUiState.Success(
            data = Image(
                description = stringResource(id = R.string.welcome_message),
                altText = stringResource(id = R.string.image),
                url = Constants.IMAGE_URL
            )
        ),
        loadData = {},
        onNavigateClick = {}
    )
}
