package com.example.vibeplayer.feature.settings.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vibeplayer.core.presentation.designsystem.theme.VibePlayerTheme
import com.example.vibeplayer.core.presentation.designsystem.theme.bodyLargeMedium
import com.example.vibeplayer.feature.settings.DurationEnum

@Composable
fun <T> SettingsControlComponent(
    title: String,
    options: List<T>,
    selected: T,
    label: (T) -> String,
    onSelect: (T) -> Unit,
) {
    Column(
        modifier = Modifier.widthIn(max = 400.dp)
    ) {
        Text(
            modifier = Modifier.padding(bottom = 10.dp),
            text = title,
            style = MaterialTheme.typography.bodyLargeMedium
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { option ->
                RadioButtonChip(
                    modifier = Modifier.weight(1f),
                    text = label(option),
                    isSelected = option == selected
                ) {
                    onSelect(option)
                }
            }
        }
    }
}

@Preview
@Composable
private fun SettingsControlComponent() {
    val selected = DurationEnum.SECONDS_30
    VibePlayerTheme {
        SettingsControlComponent(
            title = "Ignore duration less than",
            options = DurationEnum.entries,
            selected = selected,
            label = { selected.label }
        ) { }
    }
}