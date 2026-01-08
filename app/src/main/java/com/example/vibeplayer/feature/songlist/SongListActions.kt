package com.example.vibeplayer.feature.songlist

sealed interface SongListActions {
    data object SyncSongs : SongListActions
    data class OpenNowPlaying(val songId: Long) : SongListActions
    data object OnShuffleAction : SongListActions
    data object OnPlayAction : SongListActions
}