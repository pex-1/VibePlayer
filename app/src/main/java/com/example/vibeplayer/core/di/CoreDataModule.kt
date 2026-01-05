package com.example.vibeplayer.core.di

import android.content.ContentResolver
import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import com.example.vibeplayer.core.data.mediastore.LocalSongProviderImpl
import com.example.vibeplayer.core.data.repository.SongRepositoryImpl
import com.example.vibeplayer.core.data.datastore.SettingsDataStoreImpl
import com.example.vibeplayer.core.domain.LocalSongProvider
import com.example.vibeplayer.core.domain.SettingsDataStore
import com.example.vibeplayer.core.domain.SongRepository
import com.example.vibeplayer.core.playback.PlaybackController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    singleOf(::SongRepositoryImpl) bind SongRepository::class
    singleOf(::SettingsDataStoreImpl) bind SettingsDataStore::class
    singleOf(::LocalSongProviderImpl) bind LocalSongProvider::class
}

val coreModule = module {
    single { CoroutineScope(SupervisorJob() + Dispatchers.IO) }
    single<ContentResolver> {
        get<Context>().contentResolver
    }
    single {
        ExoPlayer.Builder(get()).build().apply {
            setHandleAudioBecomingNoisy(true)
        }
    }
    single {
        PlaybackController(get(), CoroutineScope(SupervisorJob() + Dispatchers.Main))
    }
}