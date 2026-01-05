package com.example.vibeplayer.feature.nowplaying

import androidx.lifecycle.ViewModel
import com.example.vibeplayer.core.playback.PlaybackController

class NowPlayingViewModel(
    private val playbackController: PlaybackController,
    songId: Long
) : ViewModel() {
    val state = playbackController.playbackState

    private var wasPlayingBeforeSeek = false
    private var isSeeking = false

    init {
        if (songId > -1) {
            playbackController.setCurrentIndex(songId.toString())
        }
        playbackController.showMiniPlayer()
    }

    fun onAction(action: NowPlayingActions) {
        when (action) {
            is NowPlayingActions.OnPlayAction -> playbackController.togglePlayPause()

            is NowPlayingActions.OnPlayNextAction -> playbackController.next()

            is NowPlayingActions.OnPlayPreviousAction -> playbackController.previous()


            is NowPlayingActions.OnSeekAction -> handleSeek(action)

            NowPlayingActions.OnRepeatAction -> playbackController.toggleRepeatMode()
            NowPlayingActions.OnShuffleAction -> playbackController.shufflePlaylist()
        }
    }

    private fun handleSeek(action: NowPlayingActions.OnSeekAction) {
        if (action.inProgress && !isSeeking) {
            isSeeking = true
            wasPlayingBeforeSeek = state.value.isPlaying
            playbackController.pause()
        }
        action.position?.let {
            playbackController.seekTo(it)
        }
        if (!action.inProgress && isSeeking) {
            isSeeking = false
            if (wasPlayingBeforeSeek) {
                playbackController.play()
            }
        }
    }
}