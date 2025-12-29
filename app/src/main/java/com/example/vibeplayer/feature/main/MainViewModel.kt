package com.example.vibeplayer.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.vibeplayer.core.domain.Result
import com.example.vibeplayer.core.domain.SongRepository
import com.example.vibeplayer.core.domain.model.Song
import com.example.vibeplayer.core.playback.PlaybackController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val songRepository: SongRepository,
    private val playbackController: PlaybackController
) : ViewModel() {

    private val syncEvents = MutableSharedFlow<Unit>()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val syncResultFlow = syncEvents
        .onStart { emit(Unit) }
        .flatMapLatest {
            songRepository.syncSongsIfEmpty()
        }

    val state: StateFlow<MainState> = combine(
        songRepository.getSongs(),
        syncResultFlow
    ) { songs, syncResult ->
        val isLoading =
            syncResult is Result.Loading || (syncResult is Result.Success && songs.isEmpty())
        MainState(isLoading = isLoading, songs = songs)
    }
        .onEach {
            if (it.isLoading.not() && it.songs.isNotEmpty()) {
                setupPlayerPlaylist(it.songs)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = MainState(isLoading = true, songs = emptyList())
        )

    private fun setupPlayerPlaylist(songs: List<Song>) {
        viewModelScope.launch {
            val mediaItems = songs.map { song ->
                MediaItem.Builder()
                    .setMediaId(song.id.toString())
                    .setUri(song.filePath)
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setTitle(song.title)
                            .setArtist(song.artist)
                            .setArtworkUri(song.embeddedArt)
                            .build()
                    )
                    .build()
            }
            playbackController.setPlaylist(mediaItems)
        }
    }

    fun onAction(action: MainActions) {
        when (action) {
            is MainActions.SyncSongs -> {
                viewModelScope.launch {
                    syncEvents.emit(Unit)
                }
            }

            is MainActions.OpenNowPlaying -> {
                playbackController.setCurrentIndex(action.songId.toString())
            }

            MainActions.OnPlayAction -> playbackController.playFromTheStart()
            MainActions.OnShuffleAction -> playbackController.shuffleAndPlay()
        }
    }

}