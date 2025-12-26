package com.example.vibeplayer.feature.nowplaying

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.vibeplayer.core.domain.SongRepository
import com.example.vibeplayer.core.playback.PlaybackController
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class NowPlayingViewModel(
    private val songId: Long,
    private val playbackController: PlaybackController,
    private val songRepository: SongRepository
) : ViewModel() {
    val state = playbackController.playbackState

    init {
        setPlaylist()
    }

    fun setPlaylist() {
        viewModelScope.launch {
            val songs = songRepository.getSongs().first()

            val mediaItems = songs.map {
                MediaItem.Builder()
                    .setMediaId(it.id.toString())
                    .setUri(it.filePath)
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setTitle(it.title)
                            .setArtist(it.artist)
                            .setArtworkUri(it.embeddedArt)
                            .build()
                    )
                    .build()
            }
            val startIndex = songs.indexOfFirst { it.id == songId }

            playbackController.setPlaylist(mediaItems, startIndex)
        }
    }

    override fun onCleared() {
        super.onCleared()
        playbackController.play()
    }

    fun onAction(action: NowPlayingActions) {
        when (action) {
            is NowPlayingActions.OnPlayAction -> {
                playbackController.play()
            }

            is NowPlayingActions.OnPlayNextAction -> {
                playbackController.next()
            }

            is NowPlayingActions.OnPlayPreviousAction -> {
                playbackController.previous()
            }
        }

    }

}