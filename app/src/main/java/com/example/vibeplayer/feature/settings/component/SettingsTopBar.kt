package com.example.vibeplayer.feature.settings.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vibeplayer.core.presentation.designsystem.theme.VibePlayerIcons
import com.example.vibeplayer.core.presentation.designsystem.theme.VibePlayerTheme
import com.example.vibeplayer.core.presentation.designsystem.theme.bodyLargeMedium
import com.example.vibeplayer.core.presentation.designsystem.theme.buttonHover
import com.example.vibeplayer.core.presentation.designsystem.theme.textPrimary
import com.example.vibeplayer.core.presentation.designsystem.theme.textSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopBar(
    onBackClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        title = {
            Text(
                text = "Scan Music",
                style = MaterialTheme.typography.bodyLargeMedium,
                color = MaterialTheme.colorScheme.textPrimary
            )
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.buttonHover),
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = VibePlayerIcons.ArrowLeft,
                    contentDescription = "Go back",
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
        SettingsTopBar { }
    }
}