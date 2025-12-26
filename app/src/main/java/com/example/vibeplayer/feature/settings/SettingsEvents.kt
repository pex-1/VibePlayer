package com.example.vibeplayer.feature.settings

sealed interface SettingsEvents {
    data object OnNavigateBack: SettingsEvents
}