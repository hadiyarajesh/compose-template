package com.hadiyarajesh.composetemplate.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
internal fun ErrorItem(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.error
) {
    Box(modifier = modifier) {
        Text(
            text = text,
            modifier = Modifier.align(Alignment.Center),
            color = color
        )
    }
}

@Composable
internal fun TextWithIcon(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    position: IconPositionInText = IconPositionInText.Trailing
) {
    val iconId = "arrow_icon"

    val annotatedText = buildAnnotatedString {
        when (position) {
            IconPositionInText.Leading -> {
                appendInlineContent(iconId, iconId)
                append(" ")
                append(text)
            }

            IconPositionInText.Trailing -> {
                append(text)
                append(" ")
                appendInlineContent(iconId, iconId)
            }
        }
    }

    val inlineContent = mapOf(
        iconId to InlineTextContent(
            Placeholder(
                width = 16.sp,
                height = 16.sp,
                placeholderVerticalAlign = PlaceholderVerticalAlign.Center
            )
        ) {
            Icon(
                modifier = modifier,
                imageVector = icon,
                contentDescription = iconId
            )
        }
    )

    Text(
        text = annotatedText,
        inlineContent = inlineContent
    )
}

internal enum class IconPositionInText {
    Leading,
    Trailing
}
