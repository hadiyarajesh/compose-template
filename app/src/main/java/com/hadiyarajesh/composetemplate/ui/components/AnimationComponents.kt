package com.hadiyarajesh.composetemplate.ui.components

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry

typealias AnimatedBackStackEntry = AnimatedContentTransitionScope<NavBackStackEntry>

const val NAVIGATION_ANIMATION_DURATION = 200

fun AnimatedBackStackEntry.slideIntoContainerAnimation(
    towards: SlideDirection = SlideDirection.End
) = slideIntoContainer(
    animationSpec = tween(
        durationMillis = NAVIGATION_ANIMATION_DURATION,
        easing = EaseIn
    ),
    towards = towards
)

fun AnimatedBackStackEntry.slideOutOfContainerAnimation(
    towards: SlideDirection = SlideDirection.Start
) = slideOutOfContainer(
    animationSpec = tween(
        durationMillis = NAVIGATION_ANIMATION_DURATION,
        easing = EaseOut
    ),
    towards = towards
)

fun AnimatedBackStackEntry.reverseSlideIntoContainerAnimation() =
    slideIntoContainerAnimation(towards = SlideDirection.Start)

fun AnimatedBackStackEntry.reverseSlideOutOfContainerAnimation() =
    slideOutOfContainerAnimation(towards = SlideDirection.End)
