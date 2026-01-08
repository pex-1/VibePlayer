package com.example.vibeplayer.core.playback

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.vibeplayer.core.domain.model.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

class PlaybackController(
    private val player: ExoPlayer
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

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

    fun showMiniPlayer() {
        _playbackState.update {
            it.copy(showMiniPlayer = true)
        }
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

    fun setPlaylist(songs: List<Song>) {
        val mediaItems = songs.map { song ->
            MediaItem.Builder()
                .setMediaId(song.songId.toString())
                .setUri(song.contentUri)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(song.title)
                        .setArtist(song.artist)
                        .setArtworkUri(song.albumArtUri)
                        .build()
                )
                .build()
        }

        playlist = mediaItems
        player.setMediaItems(playlist, 0, 0L)
        player.prepare()
    }

    fun setCurrentIndex(startIndex: String) {
        currentIndex = playlist.indexOfFirst { it.mediaId == startIndex }
        player.seekTo(currentIndex, 0L)
    }

    fun playFromTheStart() {
        player.seekTo(0, 0L)
        player.play()
    }

    fun shuffleAndPlay() {
        val startPosition = Random.nextInt(0, playlist.size - 1)
        player.seekTo(startPosition, 0L)
        player.shuffleModeEnabled = true
        player.play()
    }

    fun togglePlayPause() {
        if (player.isPlaying) {
            player.pause()
        } else {
            player.play()
        }
    }

    fun play() {
        player.play()
    }

    fun pause() {
        player.pause()
    }

    fun toggleRepeatMode() {
        val currentMode = playbackState.value.repeatMode
        val nextMode = when (currentMode) {
            RepeatMode.OFF -> RepeatMode.ALL
            RepeatMode.ALL -> RepeatMode.ONE
            RepeatMode.ONE -> RepeatMode.OFF
        }
        player.repeatMode = nextMode.playerMode
        _playbackState.update {
            it.copy(repeatMode = nextMode)
        }
    }

    fun shufflePlaylist() {
        player.shuffleModeEnabled = !player.shuffleModeEnabled
    }

    fun seekTo(position: Long) {
        player.seekTo(position)
        _playbackState.update {
            it.copy(positionMs = position)
        }
    }

    fun next() {
        if (player.hasNextMediaItem()) {
            player.seekToNextMediaItem()
        }
    }

    fun previous() {
        if (player.hasPreviousMediaItem()) {
            player.seekToPreviousMediaItem()
        }
    }

    fun releasePlayer() {
        player.release()
    }
}