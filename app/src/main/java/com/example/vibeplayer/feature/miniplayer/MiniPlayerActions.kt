package com.example.vibeplayer.feature.miniplayer

sealed interface MiniPlayerActions {
    data object OnPlayPauseAction: MiniPlayerActions
    data object OnPlayNextAction: MiniPlayerActions
    data object OnOpenNowPlayingAction: MiniPlayerActions
}