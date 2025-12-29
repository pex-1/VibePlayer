package com.example.vibeplayer.core.playback

import androidx.lifecycle.ViewModel

class PlaybackViewModel(
    private val playbackController: PlaybackController
) : ViewModel() {
    val state = playbackController.playbackState

    fun onAction(action: MiniPlayerActions) {
        when (action) {
            MiniPlayerActions.OnPlayPauseAction -> onPlayPause()
            MiniPlayerActions.OnPlayNextAction -> onPlayNext()
            else -> {}
        }
    }
    fun onPlayPause() {
        playbackController.playPause()
    }

    fun onPlayNext() {
        playbackController.next()
    }
}