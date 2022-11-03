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

    fun withArgs(vararg args: Any): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
