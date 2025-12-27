package com.example.vibeplayer.core.playback

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PlaybackController(
    private val player: ExoPlayer,
    private val scope: CoroutineScope
) {
    private var playlist: List<MediaItem> = emptyList()
    private var currentIndex = 0

    private val _playbackState = MutableStateFlow(PlaybackState())
    val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()

    private var positionJob: Job? = null

    init {
        player.addListener(object : Player.Listener {

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _playbackState.update { it.copy(isPlaying = isPlaying) }
                if (isPlaying) startPositionUpdates()
                else stopPositionUpdates()
            }

            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_READY) {
                    _playbackState.update {
                        it.copy(durationMs = player.duration.coerceAtLeast(0L))
                    }
                }
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                _playbackState.update {
                    it.copy(
                        mediaId = mediaItem?.mediaId,
                        isPlaying = player.isPlaying,
                        index = currentIndex,
                        positionMs = player.currentPosition,
                        durationMs = player.duration,
                        hasNext = currentIndex < playlist.lastIndex,
                        hasPrevious = currentIndex > 0,
                        artist = mediaItem?.mediaMetadata?.artist?.toString() ?: "Unknown Artist",
                        title = mediaItem?.mediaMetadata?.title?.toString() ?: "Unknown Title",
                        artUri = mediaItem?.mediaMetadata?.artworkUri
                    )
                }
            }
        })
    }

    private fun startPositionUpdates() {
        if (positionJob?.isActive == true) return

        positionJob = scope.launch {
            while (isActive) {
                if (player.isPlaying) {
                    _playbackState.update {
                        it.copy(positionMs = player.currentPosition)
                    }
                }
                delay(500)
            }
        }
    }

    private fun stopPositionUpdates() {
        positionJob?.cancel()
        positionJob = null
    }

    fun setPlaylist(
        items: List<MediaItem>,
        startIndex: Int = 0
    ) {
        playlist = items
        currentIndex = startIndex

        player.setMediaItems(items, startIndex, 0L)
        player.prepare()
    }

    fun play() {
        if (player.isPlaying) {
            player.pause()
        } else {
            player.play()
        }
    }

    fun stop() {
        if (player.isPlaying) {
            player.pause()
        }
    }

    fun next() {
        if (currentIndex < playlist.lastIndex) {
            currentIndex++
            player.seekToDefaultPosition(currentIndex)
        }
    }

    fun previous() {
        if (currentIndex > 0) {
            currentIndex--
            player.seekToDefaultPosition(currentIndex)
        }
    }
}