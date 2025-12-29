package com.example.vibeplayer.core.playback

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.vibeplayer.core.presentation.designsystem.components.DefaultSongIcon
import com.example.vibeplayer.core.presentation.designsystem.theme.PauseIcon
import com.example.vibeplayer.core.presentation.designsystem.theme.PlayIcon
import com.example.vibeplayer.core.presentation.designsystem.theme.SkipNextIcon
import com.example.vibeplayer.core.presentation.designsystem.theme.VibePlayerTheme
import com.example.vibeplayer.core.presentation.designsystem.theme.accent
import com.example.vibeplayer.core.presentation.designsystem.theme.bodyMediumRegular
import com.example.vibeplayer.core.presentation.designsystem.theme.surfaceBG
import com.example.vibeplayer.core.presentation.designsystem.theme.textPrimary
import com.example.vibeplayer.core.util.applyMarquee
import com.example.vibeplayer.feature.nowplaying.components.PlayerControlButton
import org.koin.androidx.compose.koinViewModel

@Composable
fun MiniPlayerRoot(
    viewModel: PlaybackViewModel = koinViewModel(),
    openNowPlaying: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    MiniPlayer(
        state = state,
        onAction = { action ->
            if (action is MiniPlayerActions.OnOpenNowPlayingAction) {
                openNowPlaying()
            } else {
                viewModel.onAction(action)
            }
        })
}

@Composable
fun MiniPlayer(
    state: PlaybackState = PlaybackState(),
    onAction: (MiniPlayerActions) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(top = 16.dp, start = 16.dp, end = 12.dp, bottom = 8.dp)
            .clickable(onClick = {
                onAction(MiniPlayerActions.OnOpenNowPlayingAction)
            })
            .padding(vertical = 12.dp)
            .height(IntrinsicSize.Max)
    ) {

        SubcomposeAsyncImage(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentDescription = null,
            model = ImageRequest.Builder(LocalContext.current)
                .data(state.artUri)
                .crossfade(true)
                .build(),
            contentScale = ContentScale.Crop,
            error = {
                DefaultSongIcon()
            },
            loading = {
                CircularProgressIndicator(
                    modifier = Modifier.padding(10.dp),
                    color = MaterialTheme.colorScheme.accent
                )
            }
        )

        Column(
            modifier = Modifier
                .padding(start = 12.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Row {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        modifier = Modifier.applyMarquee(),
                        text = state.title,
                        maxLines = 1,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        modifier = Modifier.applyMarquee(),
                        text = state.artist,
                        maxLines = 1,
                        style = MaterialTheme.typography.bodyMediumRegular
                    )
                }


                PlayerControlButton(
                    icon = if (state.isPlaying) PauseIcon else PlayIcon,
                    onButtonClick = {
                        onAction(MiniPlayerActions.OnPlayPauseAction)
                    },
                    iconTint = MaterialTheme.colorScheme.surfaceBG,
                    background = MaterialTheme.colorScheme.textPrimary,
                    padding = 8.dp
                )

                PlayerControlButton(
                    icon = SkipNextIcon,
                    onButtonClick = {
                        onAction(MiniPlayerActions.OnPlayNextAction)
                    },
                    background = Color.Transparent
                )
            }

            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
                progress = {
                    if (state.durationMs > 0) state.positionMs.toFloat() / state.durationMs else 0f
                },
                gapSize = (-10).dp,
                color = MaterialTheme.colorScheme.textPrimary,
                trackColor = MaterialTheme.colorScheme.onSurface,
                drawStopIndicator = { null }
            )
        }
    }
}

@Preview
@Composable
private fun MiniPlayerPreview() {
    VibePlayerTheme {
        MiniPlayer(
            state = PlaybackState(title = "Clocks", artist = "Coldplay")
        )
    }
}