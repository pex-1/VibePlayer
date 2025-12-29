package com.example.vibeplayer.feature.nowplaying

sealed interface NowPlayingActions {
    data object OnPlayAction : NowPlayingActions
    data object OnPlayNextAction : NowPlayingActions
    data object OnPlayPreviousAction : NowPlayingActions
    data object OnRepeatAction : NowPlayingActions
    data object OnShuffleAction : NowPlayingActions
    data class OnSeekAction(val position: Long) : NowPlayingActions
}