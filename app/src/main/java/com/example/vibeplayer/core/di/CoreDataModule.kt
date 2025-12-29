package com.example.vibeplayer.core.di

import androidx.media3.exoplayer.ExoPlayer
import com.example.vibeplayer.core.data.LocalSongProvider
import com.example.vibeplayer.core.data.SongRepositoryImpl
import com.example.vibeplayer.core.data.SongScanner
import com.example.vibeplayer.core.datastore.SettingsDataStoreImpl
import com.example.vibeplayer.core.domain.SettingsDataStore
import com.example.vibeplayer.core.domain.SongRepository
import com.example.vibeplayer.core.playback.PlaybackController
import com.example.vibeplayer.core.playback.PlaybackViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    singleOf(::SongRepositoryImpl) bind SongRepository::class
    singleOf(::SettingsDataStoreImpl) bind SettingsDataStore::class

    single { CoroutineScope(SupervisorJob() + Dispatchers.IO) }
    singleOf(::SongScanner)
    singleOf(::LocalSongProvider)

    single {
        ExoPlayer.Builder(get()).build().apply {
            setHandleAudioBecomingNoisy(true)
        }
    }
    single {
        PlaybackController(get(), CoroutineScope(SupervisorJob() + Dispatchers.Main))
    }
    viewModelOf(::PlaybackViewModel)
}