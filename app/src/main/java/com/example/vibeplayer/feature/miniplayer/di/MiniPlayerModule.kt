package com.example.vibeplayer.feature.miniplayer.di

import com.example.vibeplayer.feature.miniplayer.MiniPlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val miniPlayerModule = module {
    viewModelOf(::MiniPlayerViewModel)
}