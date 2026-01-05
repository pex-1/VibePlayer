package com.example.vibeplayer.feature.nowplaying.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vibeplayer.core.playback.PlaybackState
import com.example.vibeplayer.core.playback.RepeatMode
import com.example.vibeplayer.core.presentation.designsystem.theme.PauseIcon
import com.example.vibeplayer.core.presentation.designsystem.theme.PlayIcon
import com.example.vibeplayer.core.presentation.designsystem.theme.RepeatAllIcon
import com.example.vibeplayer.core.presentation.designsystem.theme.RepeatOffIcon
import com.example.vibeplayer.core.presentation.designsystem.theme.RepeatOneIcon
import com.example.vibeplayer.core.presentation.designsystem.theme.ShuffleIcon
import com.example.vibeplayer.core.presentation.designsystem.theme.SkipNextIcon
import com.example.vibeplayer.core.presentation.designsystem.theme.SkipPreviousIcon
import com.example.vibeplayer.core.presentation.designsystem.theme.VibePlayerTheme
import com.example.vibeplayer.core.presentation.designsystem.theme.surfaceBG
import com.example.vibeplayer.core.presentation.designsystem.theme.surfaceOutline
import com.example.vibeplayer.core.presentation.designsystem.theme.textDisabled
import com.example.vibeplayer.core.presentation.designsystem.theme.textPrimary
import com.example.vibeplayer.core.presentation.designsystem.theme.textSecondary
import com.example.vibeplayer.feature.nowplaying.NowPlayingActions

@Composable
fun NowPlayingBottomBar(
    state: PlaybackState = PlaybackState(),
    onAction: (NowPlayingActions) -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        NowPlayingProgressSlider(
            state = state,
            onAction = onAction
        )

        Row(
            modifier = Modifier
                .padding(bottom = 16.dp, top = 25.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            val iconTint = if (state.isPlaying) MaterialTheme.colorScheme.textSecondary
            else MaterialTheme.colorScheme.textDisabled
            val iconBackground = if (state.isPlaying) MaterialTheme.colorScheme.surfaceOutline
            else Color.Transparent
            PlayerControlButton(
                icon = ShuffleIcon,
                onButtonClick = {
                    onAction(NowPlayingActions.OnShuffleAction)
                },
                background = iconBackground,
                iconTint = iconTint
            )

            MainControls(modifier = Modifier.weight(1f), onAction = onAction, state = state)

            PlayerControlButton(
                icon = getRepeatModeIcon(state.repeatMode),
                onButtonClick = {
                    onAction(NowPlayingActions.OnRepeatAction)
                },
                background = iconBackground,
                iconTint = iconTint
            )
        }
    }
}

@Composable
private fun getRepeatModeIcon(repeatMode: RepeatMode) =
    when (repeatMode) {
        RepeatMode.OFF -> RepeatOffIcon
        RepeatMode.ALL -> RepeatAllIcon
        RepeatMode.ONE -> RepeatOneIcon
    }


@Composable
fun MainControls(
    modifier: Modifier = Modifier,
    onAction: (NowPlayingActions) -> Unit = {},
    state: PlaybackState
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        PlayerControlButton(
            icon = SkipPreviousIcon,
            onButtonClick = {
                onAction(NowPlayingActions.OnPlayPreviousAction)
            }
        )

        PlayerControlButton(
            icon = if (state.isPlaying) PauseIcon else PlayIcon,
            onButtonClick = {
                onAction(NowPlayingActions.OnPlayAction)
            },
            background = MaterialTheme.colorScheme.textPrimary,
            iconTint = MaterialTheme.colorScheme.surfaceBG,
            size = 60.dp,
            padding = 16.dp
        )

        PlayerControlButton(
            icon = SkipNextIcon,
            onButtonClick = {
                onAction(NowPlayingActions.OnPlayNextAction)
            }
        )
    }
}

@Preview
@Composable
private fun NowPlayingBottomBarPreview() {
    VibePlayerTheme {
        NowPlayingBottomBar()
    }
}