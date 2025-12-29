package com.example.vibeplayer.core.playback

import android.net.Uri

data class PlaybackState(
    val mediaId: String? = null,
    val index: Int = -1,
    val isPlaying: Boolean = false,
    val positionMs: Long = 0L,
    val durationMs: Long = 0L,
    val hasNext: Boolean = false,
    val hasPrevious: Boolean = false,
    val title: String = "",
    val artist: String = "",
    val artUri: Uri? = null,
    val repeatMode: RepeatMode = RepeatMode.OFF,
)
