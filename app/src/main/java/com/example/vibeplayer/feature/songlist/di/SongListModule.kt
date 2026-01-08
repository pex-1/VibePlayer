package com.example.vibeplayer.feature.songlist.di

import com.example.vibeplayer.feature.songlist.SongListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val songListModule = module {
    viewModelOf(::SongListViewModel)
}