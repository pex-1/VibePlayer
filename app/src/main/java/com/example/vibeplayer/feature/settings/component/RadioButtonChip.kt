package com.example.vibeplayer.feature.settings.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vibeplayer.core.presentation.designsystem.theme.VibePlayerTheme
import com.example.vibeplayer.core.presentation.designsystem.theme.bodyLargeMedium
import com.example.vibeplayer.core.presentation.designsystem.theme.buttonPrimary30
import com.example.vibeplayer.core.presentation.designsystem.theme.textPrimary
import com.example.vibeplayer.core.presentation.designsystem.theme.textSecondary

@Composable
fun RadioButtonChip(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    onRadioButtonClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .border(
                width = 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.buttonPrimary30
                else MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(100.dp)
            )
            .clip(RoundedCornerShape(100.dp))
            .clickable(onClick = onRadioButtonClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onRadioButtonClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = MaterialTheme.colorScheme.textSecondary
            )
        )
        Text(
            modifier = Modifier.padding(end = 14.dp),
            text = text,
            style = MaterialTheme.typography.bodyLargeMedium,
            color = MaterialTheme.colorScheme.textPrimary
        )
    }
}

@Preview
@Composable
private fun RadioButtonComponentPreview() {
    VibePlayerTheme {
        RadioButtonChip(
            text = "30s",
            isSelected = true,
            onRadioButtonClick = {}
        )
    }
}