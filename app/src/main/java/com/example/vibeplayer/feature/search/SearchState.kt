package com.example.vibeplayer.feature.search

import com.example.vibeplayer.core.domain.model.Song

data class SearchState (
    val query: String = "",
    val songs: List<Song> = emptyList(),
    val isLoading: Boolean = false,
)