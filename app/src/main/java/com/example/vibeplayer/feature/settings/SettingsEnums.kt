package com.example.vibeplayer.feature.settings

enum class SizeEnum(
    val size: Int,
    val label: String
) {
    KB_100(100, "100KB"),
    KB_500(500, "500KB");

    companion object {
        fun fromSize(value: Int) =
            entries.firstOrNull { it.size == value } ?: KB_100
    }
}

enum class DurationEnum(
    val duration: Int,
    val label: String
) {
    SECONDS_30(30, "30s"),
    SECONDS_60(60, "60s");

    companion object {
        fun fromDuration(value: Int) =
            entries.firstOrNull { it.duration == value } ?: SECONDS_30
    }
}