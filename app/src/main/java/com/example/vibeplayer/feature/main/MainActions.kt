package com.example.vibeplayer.feature.main

sealed interface MainActions {
    data object SyncSongs : MainActions
    data class OpenNowPlaying(val songId: Long) : MainActions
    data object OnShuffleAction : MainActions
    data object OnPlayAction : MainActions
}