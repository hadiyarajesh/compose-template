package com.hadiyarajesh.composetemplate.navigation

import com.hadiyarajesh.composetemplate.data.database.entity.Image
import kotlinx.serialization.Serializable

/**
 * Defines all top-level navigation destinations in the app.
 * Each destination represents a distinct screen or route.
 */
sealed interface NavDestination {
    @Serializable
    data object Home : NavDestination

    @Serializable
    data class Detail(val image: Image) : NavDestination
}
