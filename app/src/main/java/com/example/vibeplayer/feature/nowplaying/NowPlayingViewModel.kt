package com.example.vibeplayer.feature.nowplaying

import androidx.lifecycle.ViewModel
import com.example.vibeplayer.core.playback.PlaybackController

class NowPlayingViewModel(
    private val playbackController: PlaybackController
) : ViewModel() {
    val state = playbackController.playbackState

    fun onAction(action: NowPlayingActions) {
        when (action) {
            is NowPlayingActions.OnPlayAction -> {
                playbackController.playPause()
            }

            is NowPlayingActions.OnPlayNextAction -> {
                playbackController.next()
            }

            is NowPlayingActions.OnPlayPreviousAction -> {
                playbackController.previous()
            }

            is NowPlayingActions.OnSeekAction -> {
                playbackController.seekTo(action.position)
            }

            NowPlayingActions.OnRepeatAction -> playbackController.toggleRepeatMode()
            NowPlayingActions.OnShuffleAction -> playbackController.shufflePlaylist()
        }
    }
}