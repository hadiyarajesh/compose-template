package com.hadiyarajesh.composetemplate.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.hadiyarajesh.composetemplate.R
import com.hadiyarajesh.composetemplate.data.database.entity.Image

/**
 * Create a [Spacer] of given width in [dp]
 */
@Composable
internal fun RowScope.HorizontalSpacer(size: Int) = Spacer(modifier = Modifier.width(size.dp))

/**
 * Create a [Spacer] of given height in [dp]
 */
@Composable
internal fun ColumnScope.VerticalSpacer(size: Int) = Spacer(modifier = Modifier.height(size.dp))

/**
 * Create a center aligned [CircularProgressIndicator] wrapped in a [Box]
 */
@Composable
internal fun LoadingIndicator(
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    strokeWidth: Dp = 4.dp
) {
    Box(modifier = modifier) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(size)
                .align(Alignment.Center),
            color = color,
            strokeWidth = strokeWidth
        )
    }
}

@Composable
internal fun ErrorItem(text: String, modifier: Modifier = Modifier, color: Color = MaterialTheme.colorScheme.error) {
    Box(modifier = modifier) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = text,
            color = color
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TopBarWithBackButton(modifier: Modifier = Modifier, title: String, onBackClick: () -> Unit) {
    TopAppBar(
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = stringResource(R.string.go_back)
                )
            }
        },
        title = { Text(text = title) }
    )
}

/**
 * Square, rounded, elevated card that shows [image] with loading and error states.
 * [overlay] is drawn on top of the image, anchored to the bottom.
 */
@Composable
internal fun ImageHero(modifier: Modifier = Modifier, image: Image, overlay: @Composable BoxScope.() -> Unit = {}) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
    ) {
        Box(modifier = Modifier.aspectRatio(1f)) {
            SubcomposeAsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = image.url,
                contentDescription = image.description,
                contentScale = ContentScale.Crop,
                loading = { LoadingIndicator(strokeWidth = 2.dp) },
                error = { ErrorItem(text = image.altText) }
            )
            overlay()
        }
    }
}

/** Bottom-anchored caption with a dark gradient so text stays readable on any photo. */
@Composable
internal fun BoxScope.ImageCaption(text: String) {
    Text(
        modifier = Modifier
            .align(Alignment.BottomStart)
            .fillMaxWidth()
            .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.65f))))
            .padding(start = 20.dp, end = 20.dp, top = 40.dp, bottom = 16.dp),
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = Color.White,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
internal fun ClickableUrlText(modifier: Modifier = Modifier, url: String, onClick: (String) -> Unit) {
    val linkColor = MaterialTheme.colorScheme.primary
    val annotatedString =
        buildAnnotatedString {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append("${stringResource(R.string.url)}: ")
            }

            withLink(
                LinkAnnotation.Url(
                    url,
                    TextLinkStyles(
                        style = SpanStyle(
                            color = linkColor,
                            textDecoration = TextDecoration.Underline
                        )
                    )
                ) { onClick(url) }
            ) {
                append(url)
            }
        }

    Text(
        modifier = modifier,
        text = annotatedString,
        style = MaterialTheme.typography.bodyMedium
    )
}
