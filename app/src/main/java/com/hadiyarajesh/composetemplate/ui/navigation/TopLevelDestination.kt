package com.hadiyarajesh.composetemplate.ui.navigation

sealed class TopLevelDestination(
    val title: String,
    val route: String
) {
    data object Home : TopLevelDestination(
        title = "Home",
        route = "home"
    )

    data object Detail : TopLevelDestination(
        title = "Detail",
        route = "detail"
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
