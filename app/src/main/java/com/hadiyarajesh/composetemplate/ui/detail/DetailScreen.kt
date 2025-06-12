package com.hadiyarajesh.composetemplate.ui.detail

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hadiyarajesh.composetemplate.R
import com.hadiyarajesh.composetemplate.data.database.entity.Image
import com.hadiyarajesh.composetemplate.ui.components.ClickableUrlText
import com.hadiyarajesh.composetemplate.ui.components.ImageBox
import com.hadiyarajesh.composetemplate.ui.components.TopBarWithBackButton
import com.hadiyarajesh.composetemplate.ui.components.VerticalSpacer
import com.hadiyarajesh.composetemplate.utility.ImageUtility

/**
 * Entry-point composable for the Detail screen, intended to be invoked from
 * [com.hadiyarajesh.composetemplate.navigation.AppNavigation].
 *
 * It handles navigation and business logic by interacting with the ViewModel,
 * and delegates the actual UI rendering to [DetailScreenContent], making it easier
 * to separate concerns and enable previewing of the UI independently.
 */
@Composable
internal fun DetailScreenRoute(
    navController: NavController,
    image: Image
) {
    DetailScreenContent(
        image = image,
        onBackClick = { navController.popBackStack() }
    )
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
private fun DetailScreenContent(
    image: Image,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            TopBarWithBackButton(
                title = stringResource(
                    R.string.screen_name,
                    stringResource(R.string.detail)
                ),
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImageDetailView(
                modifier = Modifier.padding(16.dp),
                image = image,
                onImageUrlClick = { url ->
                    try {
                        uriHandler.openUri(url)
                    } catch (e: IllegalArgumentException) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.failed_to_open_url),
                            Toast.LENGTH_SHORT
                        ).show()
                        e.printStackTrace()
                    }
                }
            )
        }
    }
}

@Composable
private fun ImageDetailView(
    modifier: Modifier = Modifier,
    image: Image,
    onImageUrlClick: (String) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImageBox(image = image)

        VerticalSpacer(size = 24)

        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("${stringResource(R.string.description)}: ")
                }
                append(image.description)
            },
            style = MaterialTheme.typography.bodyLarge
        )

        VerticalSpacer(size = 16)

        ClickableUrlText(
            url = image.url,
            onClick = { url -> onImageUrlClick(url) }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun DetailScreenPreview() {
    DetailScreenContent(
        image = Image(
            url = ImageUtility.getRandomImageUrl(),
            description = stringResource(id = R.string.welcome_message),
            altText = stringResource(id = R.string.failed_to_load_image)
        ),
        onBackClick = {}
    )
}
