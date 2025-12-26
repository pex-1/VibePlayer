package com.example.vibeplayer.app.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface NavigationScreens: NavKey {

    @Serializable
    data object Permission : NavigationScreens

    @Serializable
    data object MainPage : NavigationScreens

    @Serializable
    data class NowPlaying(val songId: Long) : NavigationScreens

    @Serializable
    data object Settings : NavigationScreens
}