package com.hadiyarajesh.composetemplate.ui

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import com.hadiyarajesh.composetemplate.R
import com.hadiyarajesh.composetemplate.ui.components.ErrorItem
import com.hadiyarajesh.composetemplate.ui.components.TextWithIcon
import com.hadiyarajesh.composetemplate.ui.theme.AppTheme

/**
 * Compose Preview screenshot tests.
 *
 * Every `@Preview` in this source set is rendered to a reference image. Workflow:
 * - Generate/refresh reference images: `./gradlew updateDebugScreenshotTest`
 * - Verify against references:         `./gradlew validateDebugScreenshotTest`
 *
 * Reference images live under `app/src/screenshotTestDebug/reference/` and are
 * committed so `validate...` can detect unintended UI changes.
 *
 * Each preview must be annotated with `@PreviewTest` (from the
 * screenshot-validation-api artifact) to be discovered as a screenshot test.
 *
 * `dynamicColor` is disabled so renders are deterministic across devices/OS versions.
 */
@PreviewTest
@Preview(showBackground = true)
@Composable
fun ErrorItemPreview() {
    AppTheme(dynamicColor = false) {
        Surface {
            ErrorItem(text = "Something went wrong")
        }
    }
}

@PreviewTest
@Preview(showBackground = true)
@Composable
fun TextWithIconPreview() {
    AppTheme(dynamicColor = false) {
        Surface {
            TextWithIcon(
                text = "Change image",
                icon = painterResource(R.drawable.ic_refresh)
            )
        }
    }
}
