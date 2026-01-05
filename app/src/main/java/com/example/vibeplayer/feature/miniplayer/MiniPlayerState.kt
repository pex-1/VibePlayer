package com.example.vibeplayer.feature.miniplayer

import android.net.Uri

data class MiniPlayerState(
    val title: String = "",
    val artist: String = "",
    val artUri: Uri? = null,
    val isPlaying: Boolean = false,
    val isVisible: Boolean = false,
    val progress: Float = 0f,
    val durationMs: Long = 0L,
    val positionMs: Long = 0L
)
