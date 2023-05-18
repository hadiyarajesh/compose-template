package com.hadiyarajesh.composetemplate.ui.navigation

import androidx.annotation.DrawableRes
import com.hadiyarajesh.composetemplate.R

sealed class TopLevelDestination(
    val title: String,
    val route: String,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int
) {
    object Home : TopLevelDestination(
        title = "Home",
        route = "home",
        selectedIcon = R.drawable.ic_launcher_foreground,
        unselectedIcon = R.drawable.ic_launcher_foreground
    )

    object Detail : TopLevelDestination(
        title = "Detail",
        route = "detail",
        selectedIcon = R.drawable.ic_launcher_foreground,
        unselectedIcon = R.drawable.ic_launcher_foreground
    )

    /**
     * Use this function to pass arguments to navigation destination
     */
    fun withArgs(vararg args: Any): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
