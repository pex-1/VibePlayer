package com.example.vibeplayer.core.playback

sealed interface MiniPlayerActions {
    data object OnPlayPauseAction: MiniPlayerActions
    data object OnPlayNextAction: MiniPlayerActions
    data object OnOpenNowPlayingAction: MiniPlayerActions
}