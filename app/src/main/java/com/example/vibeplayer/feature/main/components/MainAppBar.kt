package com.example.vibeplayer.feature.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vibeplayer.core.presentation.designsystem.theme.VibePlayerIcons
import com.example.vibeplayer.core.presentation.designsystem.theme.VibePlayerTheme
import com.example.vibeplayer.core.presentation.designsystem.theme.accent
import com.example.vibeplayer.core.presentation.designsystem.theme.bodyLargeMedium
import com.example.vibeplayer.core.presentation.designsystem.theme.buttonHover
import com.example.vibeplayer.core.presentation.designsystem.theme.textSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(
    onSettingsClick: () -> Unit
) {

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        title = {
            Row(
                modifier = Modifier.height(24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.Bottom),
                    imageVector = VibePlayerIcons.Logo,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.accent
                )
                Text(
                    modifier = Modifier
                        .height(20.dp)
                        .align(Alignment.Top),
                    text = "VibePlayer",
                    style = MaterialTheme.typography.bodyLargeMedium,
                    color = MaterialTheme.colorScheme.accent,
                )
            }
        },
        actions = {
            IconButton(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.buttonHover),
                onClick = {
                onSettingsClick()
            }) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = VibePlayerIcons.Settings,
                    contentDescription = "Settings",
                    tint = MaterialTheme.colorScheme.textSecondary
                )
            }
        }
    )
}

@Preview
@Composable
private fun MainAppBarPreview() {
    VibePlayerTheme {
        MainAppBar { }
    }
}