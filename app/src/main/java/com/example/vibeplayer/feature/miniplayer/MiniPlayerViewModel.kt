package com.example.vibeplayer.feature.miniplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibeplayer.core.playback.PlaybackController
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MiniPlayerViewModel(
    private val playbackController: PlaybackController
) : ViewModel() {

    val state: StateFlow<MiniPlayerState> = playbackController.playbackState
        .map { playbackState ->
            MiniPlayerState(
                title = playbackState.title,
                artist = playbackState.artist,
                artUri = playbackState.artUri,
                isPlaying = playbackState.isPlaying,
                progress = if (playbackState.durationMs > 0) {
                    playbackState.positionMs.toFloat() / playbackState.durationMs
                } else {
                    0f
                },
                isVisible = playbackState.showMiniPlayer,
                durationMs = playbackState.durationMs,
                positionMs = playbackState.positionMs
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MiniPlayerState()
        )

    fun onAction(action: MiniPlayerActions) {
        when (action) {
            MiniPlayerActions.OnPlayPauseAction -> onPlayPause()
            MiniPlayerActions.OnPlayNextAction -> onPlayNext()
            else -> {}
        }
    }

    fun onPlayPause() {
        playbackController.togglePlayPause()
    }

    fun onPlayNext() {
        playbackController.next()
    }
}