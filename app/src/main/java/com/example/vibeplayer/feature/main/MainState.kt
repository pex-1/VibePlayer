package com.example.vibeplayer.feature.main

import com.example.vibeplayer.core.domain.model.Song

data class MainState(
    val isLoading: Boolean = true,
    val songs: List<Song> = emptyList()
) {
    val isEmpty: Boolean
        get() = !isLoading && songs.isEmpty()
}