package com.example.vibeplayer.feature.nowplaying.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vibeplayer.core.playback.PlaybackState
import com.example.vibeplayer.core.presentation.designsystem.theme.VibePlayerTheme
import com.example.vibeplayer.core.presentation.designsystem.theme.surfaceOutline
import com.example.vibeplayer.core.presentation.designsystem.theme.textPrimary
import com.example.vibeplayer.core.util.formatTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NowPlayingProgressSlider(
    modifier: Modifier = Modifier,
    state: PlaybackState,
    onSeek: (Long) -> Unit
) {

    val progress = if (state.durationMs > 0) state.positionMs.toFloat() / state.durationMs else 0f

    BoxWithConstraints(
        modifier = modifier
    ) {

        val fullWidth = maxWidth

        Slider(
            value = progress,
            onValueChange = { fraction ->
                onSeek((fraction * state.durationMs).toLong())
            },
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = Color.Transparent,
                activeTickColor = Color.Transparent,
                inactiveTickColor = Color.Transparent,
                disabledActiveTickColor = Color.Transparent,
                disabledInactiveTickColor = Color.Transparent,
                activeTrackColor = MaterialTheme.colorScheme.textPrimary,
                inactiveTrackColor = MaterialTheme.colorScheme.surfaceOutline
            ),
            track = { sliderState ->
                Box(
                    modifier = Modifier
                        .requiredWidth(fullWidth)
                        .height(8.dp)
                        .background(MaterialTheme.colorScheme.outline, RoundedCornerShape(100)),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .fillMaxHeight()
                            .background(
                                MaterialTheme.colorScheme.textPrimary,
                                RoundedCornerShape(100)
                            )
                    )
                }
            },
            thumb = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .wrapContentSize()
                        .background(
                            MaterialTheme.colorScheme.textPrimary,
                            RoundedCornerShape(16.dp)
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "${state.positionMs.formatTime()} / ${state.durationMs.formatTime()}",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }
            }
        )
    }
}

@Preview
@Composable
private fun NowPlayingProgressSliderPreview() {
    VibePlayerTheme {
        NowPlayingProgressSlider(state = PlaybackState()) { }
    }
}