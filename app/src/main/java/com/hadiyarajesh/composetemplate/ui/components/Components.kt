package com.hadiyarajesh.composetemplate.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
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
internal fun RowScope.HorizontalSpacer(size: Int) =
    Spacer(modifier = Modifier.width(size.dp))

/**
 * Create a [Spacer] of given height in [dp]
 */
@Composable
internal fun ColumnScope.VerticalSpacer(size: Int) =
    Spacer(modifier = Modifier.height(size.dp))

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
internal fun ErrorItem(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.error
) {
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
internal fun TopBarWithBackButton(
    modifier: Modifier = Modifier,
    title: String,
    onBackClick: () -> Unit
) {
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

@Composable
internal fun ImageBox(
    modifier: Modifier = Modifier,
    image: Image
) {
    val imageShape = RoundedCornerShape(16.dp)

    Box(
        modifier = modifier
            .size(300.dp)
            .clip(imageShape)
            .border(1.dp, Color.LightGray, imageShape)
    ) {
        SubcomposeAsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = image.url,
            contentDescription = image.description,
            loading = { LoadingIndicator(strokeWidth = 2.dp) },
            error = { ErrorItem(text = image.altText) }
        )
    }
}

@Composable
internal fun ClickableUrlText(
    modifier: Modifier = Modifier,
    url: String,
    onClick: (String) -> Unit
) {
    val annotatedString = buildAnnotatedString {
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append("${stringResource(R.string.url)}: ")
        }

        withLink(
            LinkAnnotation.Url(
                url,
                TextLinkStyles(
                    style = SpanStyle(
                        color = Color.Blue,
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
