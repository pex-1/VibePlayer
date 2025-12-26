package com.example.vibeplayer.feature.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vibeplayer.core.presentation.designsystem.buttons.VibePlayerPrimaryButton
import com.example.vibeplayer.core.presentation.designsystem.theme.VibePlayerTheme
import com.example.vibeplayer.core.presentation.designsystem.theme.bodyLargeMedium
import com.example.vibeplayer.feature.main.MainActions

@Composable
fun EmptyState(
    onAction: (MainActions) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No music found",
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            modifier = Modifier.padding(top = 4.dp, bottom = 20.dp),
            text = "Try scanning again or check your folders.",
            style = MaterialTheme.typography.bodyLargeMedium
        )
        VibePlayerPrimaryButton(
            text = "Scan again"
        ) {
            onAction(MainActions.SyncSongs)
        }
    }
}

@Preview
@Composable
private fun EmptyStatePreview() {
    VibePlayerTheme {
        EmptyState(onAction = {})
    }
}