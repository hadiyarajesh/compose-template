package com.hadiyarajesh.composetemplate.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import com.hadiyarajesh.composetemplate.data.database.entity.Image
import com.hadiyarajesh.composetemplate.ui.detail.DetailScreenContent
import com.hadiyarajesh.composetemplate.ui.home.HomeScreenContent
import com.hadiyarajesh.composetemplate.ui.home.HomeScreenUiState
import com.hadiyarajesh.composetemplate.ui.theme.AppTheme

/**
 * Screen-level Compose Preview screenshot tests.
 *
 * These render the stateless `*Content` composables with fixed sample data so the
 * golden images are deterministic. A static image URL is used on purpose (never a
 * random one) so re-generated references match. See [ComponentScreenshotTests] for
 * the workflow and requirements.
 */
private val sampleImage =
    Image(
        url = "https://example.com/sample.jpg",
        description = "A sample image",
        altText = "Failed to load image"
    )

@PreviewTest
@Preview(showSystemUi = true)
@Composable
fun HomeScreenSuccessPreview() {
    AppTheme(dynamicColor = false) {
        HomeScreenContent(
            uiState = HomeScreenUiState.Success(data = sampleImage),
            loadData = {},
            onNavigateClick = {},
            onChangeImageClick = {}
        )
    }
}

@PreviewTest
@Preview(showSystemUi = true)
@Composable
fun HomeScreenErrorPreview() {
    AppTheme(dynamicColor = false) {
        HomeScreenContent(
            uiState = HomeScreenUiState.Error(msg = "Something went wrong"),
            loadData = {},
            onNavigateClick = {},
            onChangeImageClick = {}
        )
    }
}

@PreviewTest
@Preview(showSystemUi = true)
@Composable
fun DetailScreenPreview() {
    AppTheme(dynamicColor = false) {
        DetailScreenContent(image = sampleImage, onBackClick = {})
    }
}
