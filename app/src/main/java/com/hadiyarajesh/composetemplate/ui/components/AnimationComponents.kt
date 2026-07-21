package com.hadiyarajesh.composetemplate.ui.components

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith

const val NAVIGATION_ANIMATION_DURATION = 200

/** Forward (push) transition: new screen slides in from the end edge. */
fun AnimatedContentTransitionScope<*>.slideForward(): ContentTransform = slideIntoContainer(
    towards = SlideDirection.Start,
    animationSpec = tween(NAVIGATION_ANIMATION_DURATION, easing = EaseIn)
) togetherWith
    slideOutOfContainer(
        towards = SlideDirection.Start,
        animationSpec = tween(NAVIGATION_ANIMATION_DURATION, easing = EaseOut)
    )

/** Back (pop / predictive) transition: previous screen slides back in from the start edge. */
fun AnimatedContentTransitionScope<*>.slideBack(): ContentTransform = slideIntoContainer(
    towards = SlideDirection.End,
    animationSpec = tween(NAVIGATION_ANIMATION_DURATION, easing = EaseIn)
) togetherWith
    slideOutOfContainer(
        towards = SlideDirection.End,
        animationSpec = tween(NAVIGATION_ANIMATION_DURATION, easing = EaseOut)
    )
