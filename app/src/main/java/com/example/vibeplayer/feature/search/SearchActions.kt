package com.example.vibeplayer.feature.search

sealed interface SearchActions {
    data object OnCancelClicked: SearchActions
    data object OnClearClicked: SearchActions
    data class OnQueryChanged(val query: String): SearchActions
    data class PlaySong(val songId: Long): SearchActions
}