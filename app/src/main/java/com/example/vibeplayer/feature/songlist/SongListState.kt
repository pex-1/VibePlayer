package com.example.vibeplayer.feature.songlist

import com.example.vibeplayer.core.domain.model.Song

data class SongListState(
    val isLoading: Boolean = true,
    val songs: List<Song> = emptyList(),
    val selectedTab: ContentDestination = ContentDestination.PLAYLISTS
) {
    val isEmpty = !isLoading && songs.isEmpty()
}