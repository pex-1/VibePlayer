package com.example.vibeplayer.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibeplayer.core.domain.Result
import com.example.vibeplayer.core.domain.SongRepository
import com.example.vibeplayer.core.playback.PlaybackController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
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
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = MainState(isLoading = true, songs = emptyList())
        )

    fun onAction(action: MainActions) {
        when (action) {
            is MainActions.SyncSongs -> {
                viewModelScope.launch {
                    syncEvents.emit(Unit)
                }
            }

            else -> {}
        }
    }

}