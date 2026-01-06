package com.hadiyarajesh.composetemplate.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.hadiyarajesh.composetemplate.R
import com.hadiyarajesh.composetemplate.data.database.entity.Image
import com.hadiyarajesh.composetemplate.navigation.NavDestination
import com.hadiyarajesh.composetemplate.ui.components.ErrorItem
import com.hadiyarajesh.composetemplate.ui.components.ImageBox
import com.hadiyarajesh.composetemplate.ui.components.LoadingIndicator
import com.hadiyarajesh.composetemplate.ui.components.TextWithIcon
import com.hadiyarajesh.composetemplate.ui.components.VerticalSpacer
import com.hadiyarajesh.composetemplate.utility.ImageUtility

/**
 * Entry-point composable for the Home screen, intended to be invoked from
 * [com.hadiyarajesh.composetemplate.navigation.AppNavigation].
 *
 * It handles navigation and business logic by interacting with the ViewModel,
 * and delegates the actual UI rendering to [HomeScreenContent], making it easier
 * to separate concerns and enable previewing of the UI independently.
 */
@Composable
internal fun HomeScreenRoute(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val homeScreenUiState by remember { viewModel.uiState }.collectAsStateWithLifecycle()

    HomeScreenContent(
        uiState = homeScreenUiState,
        loadData = { viewModel.loadData() },
        onNavigateClick = { image -> navController.navigate(NavDestination.Detail(image)) },
        onChangeImageClick = { image ->
            viewModel.changeImage(image)
        }
    )
}

/**
 * Stateless, preview-friendly composable that renders the Home screen UI.
 *
 * It does not require any ViewModel or navigation controller, making it
 * suitable for @Preview usage and reusable in different contexts. All actions
 * and data are passed in via parameters to promote testability and separation
 * of concerns.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContent(
    uiState: HomeScreenUiState,
    loadData: () -> Unit,
    onNavigateClick: (Image) -> Unit,
    onChangeImageClick: (Image) -> Unit
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
                    ImageAndButtonView(
                        modifier = Modifier.fillMaxSize(),
                        image = uiState.data,
                        onNavigateClick = onNavigateClick,
                        onChangeImageClick = onChangeImageClick
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
private fun ImageAndButtonView(
    modifier: Modifier = Modifier,
    image: Image,
    onNavigateClick: (Image) -> Unit,
    onChangeImageClick: (Image) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImageBox(image = image)

        VerticalSpacer(size = 16)

        Text(
            text = image.description,
            style = MaterialTheme.typography.titleSmall
        )

        VerticalSpacer(size = 16)

        OutlinedButton(onClick = { onChangeImageClick(image) }) {
            TextWithIcon(
                text = stringResource(R.string.change_image),
                icon = painterResource(R.drawable.ic_refresh)
            )
        }

        VerticalSpacer(size = 8)

        OutlinedButton(onClick = { onNavigateClick(image) }) {
            TextWithIcon(
                text = stringResource(
                    R.string.go_to_screen,
                    stringResource(id = R.string.detail)
                ),
                icon = painterResource(R.drawable.ic_arrow_forward)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreenContent(
        uiState = HomeScreenUiState.Success(
            data = Image(
                description = stringResource(id = R.string.welcome_message),
                altText = stringResource(id = R.string.failed_to_load_image),
                url = ImageUtility.getRandomImageUrl()
            )
        ),
        loadData = {},
        onNavigateClick = {},
        onChangeImageClick = {}
    )
}
