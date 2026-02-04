package com.example.vibeplayer.feature.songlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vibeplayer.core.presentation.designsystem.theme.PlusIcon
import com.example.vibeplayer.core.presentation.designsystem.theme.SurfaceOutline
import com.example.vibeplayer.core.presentation.designsystem.theme.VibePlayerTheme
import com.example.vibeplayer.core.presentation.designsystem.theme.bodyLargeMedium
import com.example.vibeplayer.core.presentation.designsystem.theme.buttonHover
import com.example.vibeplayer.core.presentation.designsystem.theme.textPrimary
import com.example.vibeplayer.core.presentation.designsystem.theme.textSecondary
import com.example.vibeplayer.feature.songlist.components.EmptyState
import com.example.vibeplayer.feature.songlist.components.LoadingState
import com.example.vibeplayer.feature.songlist.components.OutlinedIconButton
import com.example.vibeplayer.feature.songlist.components.PlaylistItem
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
        when (it) {
            is SongListActions.OpenNowPlaying -> openNowPlaying(it.songId)
            is SongListActions.OnShuffleAction, SongListActions.OnPlayAction -> {
                openNowPlaying(-1)
            }

            else -> {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongListScreen(
    state: SongListState = SongListState(),
    onAction: (SongListActions) -> Unit = {}
) {

    val tabs = ContentDestination.entries

    Column {
        PrimaryTabRow(
            selectedTabIndex = 0,
            containerColor = MaterialTheme.colorScheme.background,
            indicator = {
                TabRowDefaults.PrimaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(
                        state.selectedTab.ordinal,
                        matchContentSize = true
                    ),
                    width = Dp.Unspecified,
                    color = MaterialTheme.colorScheme.textPrimary
                )
            }
        ) {
            tabs.forEach { destination ->
                Tab(
                    selected = state.selectedTab == destination,
                    text = {
                        Text(
                            text = destination.title,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    selectedContentColor = MaterialTheme.colorScheme.textPrimary,
                    unselectedContentColor = MaterialTheme.colorScheme.textSecondary,
                    onClick = {
                        onAction(SongListActions.OnTabSelected(destination))
                    },
                )
            }

        }
        when (state.selectedTab) {
            ContentDestination.SONGS -> {
                if (state.isLoading) {
                    LoadingState()
                } else if (state.isEmpty) {
                    EmptyState(onAction = onAction)
                } else {
                    SongList(state.songs, onAction = onAction)
                }
            }

            ContentDestination.PLAYLISTS -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "1 Playlist",
                            style = MaterialTheme.typography.bodyLargeMedium,
                        )

                        IconButton(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.buttonHover),
                            onClick = {
                                onAction(SongListActions.OnCreatePlaylistClick)
                            }
                        ) {
                            Icon(
                                modifier = Modifier.size(16.dp),
                                imageVector = PlusIcon,
                                contentDescription = "Create Playlist",
                                tint = MaterialTheme.colorScheme.textSecondary
                            )
                        }
                    }
                    PlaylistItem(isFavoritesPlaylist = true)

                    Text(
                        modifier = Modifier.padding(vertical = 16.dp),
                        text = "My Playlists (0)",
                        style = MaterialTheme.typography.bodyLargeMedium,
                    )

                    if(false) {
                        OutlinedIconButton(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Create Playlist",
                            icon = PlusIcon,
                            onClick = {
                                onAction(SongListActions.OnCreatePlaylistClick)
                            }
                        )
                    }

                    PlaylistItem()
                    HorizontalDivider(thickness = 1.dp, color = SurfaceOutline)
                    PlaylistItem()
                    HorizontalDivider(thickness = 1.dp, color = SurfaceOutline)

                }


            }
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun SongListScreenPreview() {
    VibePlayerTheme {
        SongListScreen(SongListState(isLoading = false, songs = PreviewDataSource.previewSongList))
    }
}