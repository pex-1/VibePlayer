package com.example.vibeplayer.feature.songlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

class SongListViewModel(
    private val songRepository: SongRepository,
    private val playbackController: PlaybackController
) : ViewModel() {

    private val isSyncing = MutableStateFlow(true)

    private val selectedTab = MutableStateFlow(ContentDestination.SONGS)

    val state: StateFlow<SongListState> =
        combine(
            songRepository.observeSongs(),
            isSyncing,
            selectedTab
        ) { songs, syncing, selectedTab ->
            SongListState(
                isLoading = syncing,
                songs = songs,
                selectedTab = selectedTab
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SongListState(isLoading = true)
            )

    private fun observeSongsForPlayer() {
        viewModelScope.launch {
            songRepository.observeSongs()
                .distinctUntilChangedBy { songs ->
                    songs.map { it.mediaStoreId }
                }
                .collectLatest { songs ->
                    if (songs.isNotEmpty()) {
                        playbackController.setPlaylist(songs)
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
            songRepository.forceResync(applyFilters = false)
            isSyncing.value = false
        }
    }

    fun onAction(action: SongListActions) {
        when (action) {
            is SongListActions.SyncSongs -> onForceResync()

            is SongListActions.OnPlayAction -> playbackController.playFromTheStart()
            is SongListActions.OnShuffleAction -> playbackController.shuffleAndPlay()
            is SongListActions.OnTabSelected -> selectedTab.value = action.destination
            else -> {}
        }
    }

    override fun onCleared() {
        playbackController.releasePlayer()
    }
}