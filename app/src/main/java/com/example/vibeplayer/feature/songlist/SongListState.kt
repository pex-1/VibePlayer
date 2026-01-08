package com.example.vibeplayer.feature.songlist

import com.example.vibeplayer.core.domain.model.Song

data class SongListState(
    val isLoading: Boolean = true,
    val songs: List<Song> = emptyList()
) {
    val isEmpty = !isLoading && songs.isEmpty()
}