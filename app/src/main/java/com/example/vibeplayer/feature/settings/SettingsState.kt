package com.example.vibeplayer.feature.settings

data class SettingsState(
    val duration: DurationEnum = DurationEnum.SECONDS_30,
    val size: SizeEnum = SizeEnum.KB_100,
    val isScanning: Boolean = false,
)
