package com.hadiyarajesh.composetemplate.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hadiyarajesh.composetemplate.R
import com.hadiyarajesh.composetemplate.data.database.entity.Image
import com.hadiyarajesh.composetemplate.ui.components.ErrorItem
import com.hadiyarajesh.composetemplate.ui.components.ImageCaption
import com.hadiyarajesh.composetemplate.ui.components.ImageHero
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
internal fun HomeScreenRoute(onNavigateToDetail: (Image) -> Unit, viewModel: HomeViewModel = hiltViewModel()) {
    val homeScreenUiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreenContent(
        uiState = homeScreenUiState,
        loadData = viewModel::start,
        onNavigateClick = onNavigateToDetail,
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
internal fun HomeScreenContent(
    uiState: HomeScreenUiState,
    loadData: () -> Unit,
    onNavigateClick: (Image) -> Unit,
    onChangeImageClick: (Image) -> Unit
) {
    LaunchedEffect(Unit) {
        loadData()
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        fontWeight = FontWeight.Bold
                    )
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        when (uiState) {
            is HomeScreenUiState.Initial -> Unit

            is HomeScreenUiState.Loading -> {
                LoadingIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            is HomeScreenUiState.Success -> {
                ImageAndButtonView(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    image = uiState.data,
                    onNavigateClick = onNavigateClick,
                    onChangeImageClick = onChangeImageClick
                )
            }

            is HomeScreenUiState.Error -> {
                ErrorItem(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                    text = uiState.msg
                )
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
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        ImageHero(image = image, overlay = { ImageCaption(text = image.description) })

        VerticalSpacer(size = 24)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FilledTonalButton(
                modifier = Modifier.weight(1f),
                onClick = { onChangeImageClick(image) }
            ) {
                TextWithIcon(
                    text = stringResource(R.string.new_image),
                    icon = painterResource(R.drawable.ic_refresh)
                )
            }

            Button(
                modifier = Modifier.weight(1f),
                onClick = { onNavigateClick(image) }
            ) {
                TextWithIcon(
                    text = stringResource(R.string.view_details),
                    icon = painterResource(R.drawable.ic_arrow_forward)
                )
            }
        }

        VerticalSpacer(size = 16)

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                text = stringResource(R.string.welcome_message),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreenContent(
        uiState = HomeScreenUiState.Success(
            data =
            Image(
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
