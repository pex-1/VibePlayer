package com.example.vibeplayer.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.vibeplayer.core.domain.SongRepository
import com.example.vibeplayer.core.domain.model.Song
import com.example.vibeplayer.core.playback.PlaybackController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val songRepository: SongRepository,
    private val playbackController: PlaybackController
) : ViewModel() {

    private val isSyncing = MutableStateFlow(true)

    val state: StateFlow<MainState> =
        combine(
            songRepository.observeSongs(),
            isSyncing
        ) { songs, syncing ->
            MainState(
                isLoading = syncing,
                songs = songs
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = MainState(isLoading = true)
            )

    private fun observeSongsForPlayer() {
        viewModelScope.launch {
            songRepository.observeSongs()
                .distinctUntilChangedBy { songs ->
                    songs.map { it.mediaStoreId }
                }
                .collectLatest { songs ->
                    if (songs.isNotEmpty()) {
                        setupPlayerPlaylist(songs)
                    }
                }
        }
    }

    init {
        syncOnStart()
        observeSongsForPlayer()
    }

    private fun syncOnStart() {
        viewModelScope.launch {
            isSyncing.value = true
            songRepository.syncOnAppStart()
            isSyncing.value = false
        }
    }

    fun onForceResync() {
        viewModelScope.launch {
            isSyncing.value = true
            songRepository.syncSongs(applyFilters = false)
            isSyncing.value = false
        }
    }

    private fun setupPlayerPlaylist(songs: List<Song>) {
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
        playbackController.setPlaylist(mediaItems)
    }


    fun onAction(action: MainActions) {
        when (action) {
            is MainActions.SyncSongs -> onForceResync()

            is MainActions.OpenNowPlaying -> {
                playbackController.setCurrentIndex(action.songId.toString())
            }

            MainActions.OnPlayAction -> playbackController.playFromTheStart()
            MainActions.OnShuffleAction -> playbackController.shuffleAndPlay()
        }
    }
}