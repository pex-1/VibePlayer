package com.example.vibeplayer.feature.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vibeplayer.core.presentation.designsystem.theme.VibePlayerTheme
import com.example.vibeplayer.feature.main.components.EmptyState
import com.example.vibeplayer.feature.main.components.LoadingState
import com.example.vibeplayer.feature.main.components.SongList
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreenRoot(
    viewModel: MainViewModel = koinViewModel(),
    openNowPlaying: () -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    MainScreen(state) {
        viewModel.onAction(it)
        if (it !is MainActions.SyncSongs) {
            openNowPlaying()
        }
    }
}

@Composable
fun MainScreen(
    state: MainState = MainState(),
    onAction: (MainActions) -> Unit = {}
) {
    if (state.isLoading) {
        LoadingState()
    } else if (state.songs.isEmpty()) {
        EmptyState(onAction = onAction)
    } else {
        SongList(state.songs, onAction = onAction)
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    VibePlayerTheme {
        MainScreen(MainState(isLoading = false, songs = PreviewDataSource.previewSongList))
    }
}