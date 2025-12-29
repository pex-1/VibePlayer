package com.example.vibeplayer.feature.nowplaying.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.vibeplayer.core.presentation.designsystem.theme.surfaceOutline
import com.example.vibeplayer.core.presentation.designsystem.theme.textSecondary

@Composable
fun PlayerControlButton(
    icon: ImageVector,
    size: Dp = 44.dp,
    padding: Dp = 0.dp,
    background: Color = MaterialTheme.colorScheme.surfaceOutline,
    iconTint: Color = MaterialTheme.colorScheme.textSecondary,
    onButtonClick: () -> Unit = {}
) {
    IconButton(
        modifier = Modifier
            .padding(horizontal = padding)
            .size(size)
            .clip(CircleShape)
            .background(background),
        onClick = {
            onButtonClick()
        }
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = icon,
            contentDescription = null,
            tint = iconTint
        )
    }
}