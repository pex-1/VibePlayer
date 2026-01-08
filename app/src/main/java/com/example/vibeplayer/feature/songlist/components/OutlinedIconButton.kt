package com.example.vibeplayer.feature.songlist.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vibeplayer.core.presentation.designsystem.theme.ShuffleIcon
import com.example.vibeplayer.core.presentation.designsystem.theme.VibePlayerTheme
import com.example.vibeplayer.core.presentation.designsystem.theme.bodyLargeMedium
import com.example.vibeplayer.core.presentation.designsystem.theme.surfaceOutline
import com.example.vibeplayer.core.presentation.designsystem.theme.textPrimary

@Composable
fun OutlinedIconButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    onClick: () -> Unit = {}
) {
    IconButton(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surfaceOutline,
                shape = RoundedCornerShape(100.dp)
            ),
        onClick = onClick,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.textPrimary
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLargeMedium,
                color = MaterialTheme.colorScheme.textPrimary
            )
        }
    }
}

@Preview
@Composable
private fun OutlinedIconButtonPreview() {
    VibePlayerTheme {
        OutlinedIconButton(text = "Shuffle", icon = ShuffleIcon)
    }
}