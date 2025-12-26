package com.example.vibeplayer.core.util

import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.basicMarquee
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun Long.toMinutesSeconds(): String {
    val minutes = this / 60
    val seconds = (this % 60).toString().padStart(2, '0')
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