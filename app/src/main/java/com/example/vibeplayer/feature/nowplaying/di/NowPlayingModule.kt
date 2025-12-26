package com.example.vibeplayer.feature.nowplaying.di

import com.example.vibeplayer.feature.nowplaying.NowPlayingViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val nowPlayingModule = module {
    viewModelOf(::NowPlayingViewModel)
}