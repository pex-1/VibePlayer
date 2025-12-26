package com.example.vibeplayer.core.presentation.designsystem.buttons

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.vibeplayer.core.presentation.designsystem.theme.ButtonHover
import com.example.vibeplayer.core.presentation.designsystem.theme.ShadowColor
import com.example.vibeplayer.core.presentation.designsystem.theme.VibePlayerTheme
import com.example.vibeplayer.core.presentation.designsystem.theme.textDisabled
import com.example.vibeplayer.core.presentation.designsystem.theme.textPrimary

@Composable
fun VibePlayerPrimaryButton(
    modifier: Modifier = Modifier,
    text: String = "Button",
    scanningText: String = "",
    isScanning: Boolean = false,
    onclick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val showShadow = (isScanning || isPressed).not()

    Button(
        modifier = modifier
            .then(
                if (showShadow) {
                    Modifier.dropShadow(
                        shape = RoundedCornerShape(100.dp),
                        shadow = Shadow(
                            radius = 8.dp,
                            color = ShadowColor,
                            spread = 2.dp,
                            offset = DpOffset(y = 2.dp, x = 0.dp),
                        )
                    )
                } else Modifier
            ),
        shape = RoundedCornerShape(100.dp),
        contentPadding = PaddingValues(vertical = 11.dp, horizontal = 24.dp),
        onClick = { onclick() },
        enabled = !isScanning,
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = ButtonHover,
        )

    ) {
        val buttonColor = if (isScanning) MaterialTheme.colorScheme.textDisabled
        else MaterialTheme.colorScheme.textPrimary
        val buttonText = if (isScanning) scanningText else text
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (isScanning) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = buttonColor
                )
            }
            Text(
                text = buttonText,
                style = MaterialTheme.typography.bodyLarge,
                color = buttonColor
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VibePlayerPrimaryButtonPreview() {
    VibePlayerTheme {
        VibePlayerPrimaryButton(isScanning = true)
    }
}

@Preview(showBackground = true)
@Composable
private fun VibePlayerPrimaryButtonDisabledPreview() {
    VibePlayerTheme {
        VibePlayerPrimaryButton(isScanning = false)
    }
}