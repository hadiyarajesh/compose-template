package com.hadiyarajesh.composetemplate.ui.components

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.sp

/**
 * A composable function that displays a [text] with an [icon], either at the leading
 * or trailing position.
 *
 * Useful for showing UI elements like labels, buttons, or clickable rows with icons
 * aligned next to the text in a seamless way using inline content.
 *
 * @param modifier Modifier to be applied to the icon.
 * @param text The text to display alongside the icon.
 * @param icon The [ImageVector] representing the icon to show.
 * @param position Controls whether the icon appears before or after the text.
 */
@Composable
internal fun TextWithIcon(
    modifier: Modifier = Modifier,
    text: String,
    icon: Painter,
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
                painter = icon,
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
