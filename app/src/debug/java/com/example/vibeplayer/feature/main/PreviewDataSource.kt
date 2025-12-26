package com.example.vibeplayer.feature.main

import com.example.vibeplayer.core.domain.model.Song

object PreviewDataSource {
    val previewSongList = listOf(
        Song(
            id = 1,
            title = "Midnight Drive",
            artist = "Neon Skyline",
            filePath = "/storage/emulated/0/Music/midnight_drive.mp3",
            duration = 198L // 3:18
        ),
        Song(
            id = 2,
            title = "Ocean Breeze",
            artist = "Blue Horizon",
            filePath = "/storage/emulated/0/Music/ocean_breeze.mp3",
            duration = 242L // 4:02
        ),
        Song(
            id = 3,
            title = "Golden Hour",
            artist = "Sunset Avenue",
            filePath = "/storage/emulated/0/Music/golden_hour.mp3",
            duration = 215L // 3:35
        ),
        Song(
            id = 4,
            title = "Lost in Space",
            artist = "Stellar Drift",
            filePath = "/storage/emulated/0/Music/lost_in_space.mp3",
            duration = 185L // 3:05
        ),
        Song(
            id = 5,
            title = "Echoes",
            artist = "Nova Lights",
            filePath = "/storage/emulated/0/Music/echoes.mp3",
            duration = 227L // 3:47
        ),
        Song(
            id = 6,
            title = "Waves",
            artist = "Calm Shores",
            filePath = "/storage/emulated/0/Music/waves.mp3",
            duration = 264L // 4:24
        ),
        Song(
            id = 7,
            title = "City Nights",
            artist = "Urban Echo",
            filePath = "/storage/emulated/0/Music/city_nights.mp3",
            duration = 201L // 3:21
        ),
        Song(
            id = 8,
            title = "Shadow Walk",
            artist = "Dark Lantern",
            filePath = "/storage/emulated/0/Music/shadow_walk.mp3",
            duration = 233L // 3:53
        ),
        Song(
            id = 9,
            title = "Running Free",
            artist = "Open Road",
            filePath = "/storage/emulated/0/Music/running_free.mp3",
            duration = 178L // 2:58
        ),
        Song(
            id = 10,
            title = "Dreamscape",
            artist = "Cloud Harbor",
            filePath = "/storage/emulated/0/Music/dreamscape.mp3",
            duration = 251L // 4:11
        )
    )
}