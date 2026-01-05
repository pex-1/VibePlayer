package com.example.vibeplayer.core.util

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.basicMarquee
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.dp

fun Long.toMinutesSeconds(): String {
    val totalSeconds = this / 1000
    val minutes = totalSeconds / 60
    val seconds = (totalSeconds % 60).toString().padStart(2, '0')
    return "$minutes:$seconds"
}

fun Modifier.applyMarquee(): Modifier {
    return this.basicMarquee(
        animationMode = MarqueeAnimationMode.Immediately,
        iterations = Int.MAX_VALUE,
        initialDelayMillis = 1000,
        velocity = 30.dp,
        spacing = MarqueeSpacing(40.dp)
    )
}

fun Long.formatTime(): String {
    val totalSeconds = this / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}

fun Modifier.miniPlayerTransition(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    key: String,
    durationMillis: Int = 300
): Modifier = with(sharedTransitionScope) {

    composed {
        val sharedContentState = rememberSharedContentState(key = key)

        this@composed.sharedElement(
            sharedContentState = sharedContentState,
            animatedVisibilityScope = animatedVisibilityScope,
            boundsTransform = { _, _ ->
                tween(durationMillis = durationMillis)
            }
        )
    }
}