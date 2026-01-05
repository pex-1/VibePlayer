package com.example.vibeplayer.feature.settings

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vibeplayer.core.presentation.designsystem.buttons.VibePlayerPrimaryButton
import com.example.vibeplayer.core.presentation.designsystem.theme.RadarImage
import com.example.vibeplayer.core.presentation.designsystem.theme.VibePlayerTheme
import com.example.vibeplayer.core.presentation.util.ObserveAsEvents
import com.example.vibeplayer.feature.settings.component.SettingsControlComponent
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreenRoot(
    viewMode: SettingsViewModel = koinViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewMode.state.collectAsStateWithLifecycle()
    ObserveAsEvents(flow = viewMode.event) { event ->
        when (event) {
            is SettingsEvents.OnNavigateBack -> onBackClick()
        }
    }

    SettingsScreen(
        state = state,
        onAction = { action ->
            if (action is SettingsAction.OnBackClickAction) {
                onBackClick()
            } else {
                viewMode.onAction(action)
            }
        }
    )
}

@Composable
fun SettingsScreen(
    state: SettingsState = SettingsState(),
    onAction: (SettingsAction) -> Unit = {}
) {

    var rotationAngle by remember { mutableFloatStateOf(0f) }

    if (state.isScanning) {
        val infiniteTransition = rememberInfiniteTransition(label = "radar_rotation")
        val animatedAngle by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(1500, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "radar_angle"
        )
        rotationAngle = animatedAngle
    } else {
        rotationAngle = 0f
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {


        Column(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = 400.dp)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                modifier = Modifier
                    .rotate(rotationAngle)
                    .size(140.dp),
                painter = RadarImage,
                contentDescription = "Radar image"
            )

            Spacer(modifier = Modifier.size(24.dp))

            SettingsControlComponent(
                title = "Ignore duration less than",
                options = DurationEnum.entries,
                selected = state.duration,
                label = { it.label }
            ) { selected ->
                onAction(SettingsAction.OnDurationChange(selected))
            }

            Spacer(modifier = Modifier.size(16.dp))

            SettingsControlComponent(
                title = "Ignore size less than",
                options = SizeEnum.entries,
                selected = state.size,
                label = { it.label }
            ) { selected ->
                onAction(SettingsAction.OnSizeChange(selected))
            }

            VibePlayerPrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                text = "Scan",
                scanningText = "Scanning",
                isScanning = state.isScanning,
                onclick = {
                    onAction(SettingsAction.OnScanAction)
                }
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun SettingsScreenPreview() {
    VibePlayerTheme {
        SettingsScreen { }
    }
}