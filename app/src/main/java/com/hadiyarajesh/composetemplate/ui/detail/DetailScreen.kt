package com.hadiyarajesh.composetemplate.ui.detail

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hadiyarajesh.composetemplate.R
import com.hadiyarajesh.composetemplate.data.database.entity.Image
import com.hadiyarajesh.composetemplate.ui.components.ClickableUrlText
import com.hadiyarajesh.composetemplate.ui.components.ImageHero
import com.hadiyarajesh.composetemplate.ui.components.TopBarWithBackButton
import com.hadiyarajesh.composetemplate.ui.components.VerticalSpacer
import com.hadiyarajesh.composetemplate.utility.ImageUtility
import java.net.URI

/**
 * Entry-point composable for the Detail screen, intended to be invoked from
 * [com.hadiyarajesh.composetemplate.navigation.AppNavigation].
 *
 * It handles navigation and business logic by interacting with the ViewModel,
 * and delegates the actual UI rendering to [DetailScreenContent], making it easier
 * to separate concerns and enable previewing of the UI independently.
 */
@Composable
internal fun DetailScreenRoute(image: Image, onBack: () -> Unit, viewModel: DetailViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(viewModel, image) { viewModel.start(image) }

    LaunchedEffect(viewModel) {
        viewModel.openUrlFailedFlow.collect {
            Toast.makeText(context, context.getString(R.string.failed_to_open_url), Toast.LENGTH_SHORT).show()
        }
    }

    // Empty for the single frame before the Start action is processed.
    uiState.image?.let { selected ->
        DetailScreenContent(
            image = selected,
            onBackClick = onBack,
            onOpenUrlFailed = viewModel::notifyOpenUrlFailed
        )
    }
}

/**
 * Stateless, preview-friendly composable that renders the Detail screen UI.
 *
 * It does not require any ViewModel or navigation controller, making it
 * suitable for @Preview usage and reusable in different contexts. All actions
 * and data are passed in via parameters to promote testability and separation
 * of concerns.
 */
@Composable
internal fun DetailScreenContent(image: Image, onBackClick: () -> Unit, onOpenUrlFailed: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    val openUrl: (String) -> Unit = { url ->
        try {
            uriHandler.openUri(url)
        } catch (e: IllegalArgumentException) {
            onOpenUrlFailed()
            e.printStackTrace()
        }
    }

    Scaffold(
        topBar = {
            TopBarWithBackButton(
                title = stringResource(R.string.detail),
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            Surface(tonalElevation = 3.dp) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    onClick = { openUrl(image.url) }
                ) {
                    Text(text = stringResource(R.string.open_in_browser))
                }
            }
        }
    ) { innerPadding ->
        ImageDetailView(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(16.dp),
            image = image,
            onImageUrlClick = openUrl
        )
    }
}

@Composable
private fun ImageDetailView(modifier: Modifier = Modifier, image: Image, onImageUrlClick: (String) -> Unit) {
    Column(modifier = modifier) {
        ImageHero(image = image)

        VerticalSpacer(size = 16)

        image.host()?.let { host ->
            AssistChip(
                onClick = {},
                label = { Text(text = stringResource(R.string.source_host, host)) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    labelColor = MaterialTheme.colorScheme.onTertiaryContainer
                ),
                border = null
            )

            VerticalSpacer(size = 16)
        }

        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.72f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val contentColor = MaterialTheme.colorScheme.onSecondaryContainer

                Text(
                    text = stringResource(R.string.about),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )

                Text(
                    text = image.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = contentColor
                )

                VerticalSpacer(size = 8)

                Text(
                    text = stringResource(R.string.links),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )

                ClickableUrlText(
                    url = image.url,
                    onClick = onImageUrlClick
                )
            }
        }
    }
}

private fun Image.host(): String? = runCatching { URI(url).host }.getOrNull()

@Preview(showSystemUi = true)
@Composable
fun DetailScreenPreview() {
    DetailScreenContent(
        image =
        Image(
            url = ImageUtility.getRandomImageUrl(),
            description = stringResource(id = R.string.welcome_message),
            altText = stringResource(id = R.string.failed_to_load_image)
        ),
        onBackClick = {},
        onOpenUrlFailed = {}
    )
}
