package com.example.vibeplayer.core.playback

import androidx.media3.common.Player

enum class RepeatMode(val playerMode: Int) {
    OFF(Player.REPEAT_MODE_OFF),
    ONE(Player.REPEAT_MODE_ONE),
    ALL(Player.REPEAT_MODE_ALL)
}