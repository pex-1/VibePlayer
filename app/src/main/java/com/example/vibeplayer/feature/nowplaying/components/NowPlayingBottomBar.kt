package com.example.vibeplayer.feature.nowplaying.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vibeplayer.core.presentation.designsystem.theme.VibePlayerIcons
import com.example.vibeplayer.core.presentation.designsystem.theme.VibePlayerTheme
import com.example.vibeplayer.core.presentation.designsystem.theme.surfaceBG
import com.example.vibeplayer.core.presentation.designsystem.theme.surfaceOutline
import com.example.vibeplayer.core.presentation.designsystem.theme.textPrimary
import com.example.vibeplayer.core.presentation.designsystem.theme.textSecondary
import com.example.vibeplayer.feature.nowplaying.NowPlayingActions

@Composable
fun NowPlayingBottomBar(
    isPlaying: Boolean = false,
    progress: Float = 0f,
    onAction: (NowPlayingActions) -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            progress = {
                progress
            },
            color = MaterialTheme.colorScheme.textPrimary,
            trackColor = MaterialTheme.colorScheme.surfaceOutline,
            strokeCap = ProgressIndicatorDefaults.LinearStrokeCap
        )

        Row(
            modifier = Modifier
                .padding(bottom = 16.dp, top = 25.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceOutline),
                onClick = {
                    onAction(NowPlayingActions.OnPlayPreviousAction)
                }
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = VibePlayerIcons.SkipPrevious,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.textSecondary
                )
            }

            IconButton(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.textPrimary),
                onClick = {
                    onAction(NowPlayingActions.OnPlayAction)
                }
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = if (isPlaying) VibePlayerIcons.Pause else VibePlayerIcons.Play,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.surfaceBG
                )
            }

            IconButton(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceOutline),
                onClick = {
                    onAction(NowPlayingActions.OnPlayNextAction)
                }
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = VibePlayerIcons.SkipNext,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.textSecondary
                )
            }
        }
    }
}

@Preview
@Composable
private fun NowPlayingBottomBarPreview() {
    VibePlayerTheme {
        NowPlayingBottomBar()
    }
}