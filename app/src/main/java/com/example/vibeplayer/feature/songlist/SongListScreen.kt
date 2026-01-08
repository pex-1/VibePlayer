package com.example.vibeplayer.feature.songlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vibeplayer.core.presentation.designsystem.theme.VibePlayerTheme
import com.example.vibeplayer.feature.songlist.components.EmptyState
import com.example.vibeplayer.feature.songlist.components.LoadingState
import com.example.vibeplayer.feature.songlist.components.SongList
import org.koin.androidx.compose.koinViewModel

@Composable
fun SongListScreenRoot(
    viewModel: SongListViewModel = koinViewModel(),
    openNowPlaying: (Long) -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SongListScreen(state) {
        viewModel.onAction(it)
        when(it) {
            is SongListActions.OpenNowPlaying -> openNowPlaying(it.songId)
            is SongListActions.OnShuffleAction, SongListActions.OnPlayAction -> {
                openNowPlaying(-1)
            }
            else -> {}
        }
    }
}

@Composable
fun SongListScreen(
    state: SongListState = SongListState(),
    onAction: (SongListActions) -> Unit = {}
) {
    if (state.isLoading) {
        LoadingState()
    } else if (state.isEmpty) {
        EmptyState(onAction = onAction)
    } else {
        SongList(state.songs, onAction = onAction)
    }
}

@Preview(showBackground = true)
@Composable
private fun SongListScreenPreview() {
    VibePlayerTheme {
        SongListScreen(SongListState(isLoading = false, songs = PreviewDataSource.previewSongList))
    }
}