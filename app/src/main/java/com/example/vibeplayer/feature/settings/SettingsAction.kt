package com.example.vibeplayer.feature.settings

sealed interface SettingsAction {
    data object OnBackClickAction : SettingsAction
    data object OnScanAction : SettingsAction

    data class OnDurationChange(val selected: DurationEnum) : SettingsAction
    data class OnSizeChange(val selected: SizeEnum) : SettingsAction

}