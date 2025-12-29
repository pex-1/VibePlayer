package com.example.vibeplayer.feature.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vibeplayer.core.presentation.designsystem.components.SongListItem
import com.example.vibeplayer.core.presentation.designsystem.theme.ClearIcon
import com.example.vibeplayer.core.presentation.designsystem.theme.SearchIcon
import com.example.vibeplayer.core.presentation.designsystem.theme.SurfaceOutline
import com.example.vibeplayer.core.presentation.designsystem.theme.VibePlayerTheme
import com.example.vibeplayer.core.presentation.designsystem.theme.bodyLargeMedium
import com.example.vibeplayer.core.presentation.designsystem.theme.bodyLargeRegular
import com.example.vibeplayer.core.presentation.designsystem.theme.buttonHover
import com.example.vibeplayer.core.presentation.designsystem.theme.surfaceOutline
import com.example.vibeplayer.core.presentation.designsystem.theme.textPrimary
import com.example.vibeplayer.core.presentation.designsystem.theme.textSecondary
import com.example.vibeplayer.feature.main.PreviewDataSource.previewSongList
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreenRoot(
    viewModel: SearchViewModel = koinViewModel(),
    onCancelClick: () -> Unit = {},
    onPlaySong: (Long) -> Unit = {}
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    SearchScreen(state) { action ->
        when (action) {
            is SearchActions.OnCancelClicked -> {
                onCancelClick()
            }

            is SearchActions.PlaySong -> {
                onPlaySong(action.songId)
            }

            else -> {
                viewModel.onAction(action)
            }
        }
    }
}

@Composable
fun SearchScreen(
    state: SearchState = SearchState(),
    onAction: (SearchActions) -> Unit = {}
) {

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester),
                    value = state.query,
                    onValueChange = {
                        onAction(SearchActions.OnQueryChanged(it))
                    },
                    shape = RoundedCornerShape(100.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = SearchIcon,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.textSecondary
                        )

                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                onAction(SearchActions.OnClearClicked)
                            }
                        ) {
                            Icon(
                                imageVector = ClearIcon,
                                contentDescription = "Clear",
                                tint = MaterialTheme.colorScheme.textSecondary
                            )
                        }
                    },
                    placeholder = {
                        Text(
                            modifier = Modifier,
                            text = "Search",
                            style = MaterialTheme.typography.bodyLargeRegular
                        )
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.buttonHover,
                        unfocusedContainerColor = MaterialTheme.colorScheme.buttonHover,
                        focusedBorderColor = MaterialTheme.colorScheme.surfaceOutline,
                        unfocusedBorderColor = MaterialTheme.colorScheme.surfaceOutline,
                        focusedTextColor = MaterialTheme.colorScheme.textPrimary,
                        unfocusedTextColor = MaterialTheme.colorScheme.textPrimary,
                        focusedLeadingIconColor = MaterialTheme.colorScheme.textSecondary,
                        unfocusedLeadingIconColor = MaterialTheme.colorScheme.textSecondary,
                        focusedTrailingIconColor = MaterialTheme.colorScheme.textSecondary,
                        unfocusedTrailingIconColor = MaterialTheme.colorScheme.textSecondary,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.textSecondary,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.textSecondary,
                        cursorColor = MaterialTheme.colorScheme.textPrimary
                    )

                )

                Text(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .clickable(onClick = {
                            onAction(SearchActions.OnCancelClicked)
                        }),
                    text = "Cancel",
                    style = MaterialTheme.typography.bodyLargeMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            if (state.songs.isEmpty()) {
                Text(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    text = "No results found.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLargeRegular,

                    )
            }
            val lazyListState = rememberLazyListState()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                state = lazyListState
            ) {
                itemsIndexed(
                    items = state.songs,
                    key = { _, song -> song.id }
                ) { index, song ->
                    SongListItem(song) {
                onAction(SearchActions.PlaySong(it))
                    }
                    HorizontalDivider(thickness = 1.dp, color = SurfaceOutline)
                }
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
                    .size(200.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview
@Composable
private fun SearchScreenPreview() {
    VibePlayerTheme {
        SearchScreen(state = SearchState(songs = previewSongList))
    }
}