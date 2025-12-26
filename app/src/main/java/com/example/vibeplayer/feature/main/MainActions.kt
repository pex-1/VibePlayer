package com.example.vibeplayer.feature.main

sealed interface MainActions {
    data object SyncSongs : MainActions
    data class PlaySong(val songId: Long) : MainActions
}