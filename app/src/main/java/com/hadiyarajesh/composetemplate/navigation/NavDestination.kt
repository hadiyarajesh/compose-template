package com.hadiyarajesh.composetemplate.navigation

import androidx.navigation3.runtime.NavKey
import com.hadiyarajesh.composetemplate.data.database.entity.Image
import kotlinx.serialization.Serializable

/**
 * All navigation destinations in the app. Each is a Nav3 [NavKey] placed on the
 * developer-owned back stack. Keys stay @Serializable so the back stack survives
 * process death.
 */
sealed interface NavDestination : NavKey {
    @Serializable
    data object Home : NavDestination

    @Serializable
    data class Detail(val image: Image) : NavDestination
}
