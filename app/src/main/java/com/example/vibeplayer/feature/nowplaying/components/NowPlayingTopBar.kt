package com.example.vibeplayer.feature.nowplaying.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vibeplayer.core.presentation.designsystem.theme.ArrowLeftIcon
import com.example.vibeplayer.core.presentation.designsystem.theme.ChevronDownIcon
import com.example.vibeplayer.core.presentation.designsystem.theme.VibePlayerTheme
import com.example.vibeplayer.core.presentation.designsystem.theme.buttonHover
import com.example.vibeplayer.core.presentation.designsystem.theme.textSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NowPlayingTopBar(
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {},
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        navigationIcon = {
            IconButton(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.buttonHover),
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = ChevronDownIcon,
                    contentDescription = "Minimize screen",
                    tint = MaterialTheme.colorScheme.textSecondary
                )
            }
        }
    )
}

@Preview
@Composable
private fun SettingsTopBarPreview() {
    VibePlayerTheme {
        NowPlayingTopBar { }
    }
}