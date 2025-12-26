package com.example.vibeplayer.app.di

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import com.example.vibeplayer.app.VibePlayerApp
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module{
    single<VibePlayerApp> { androidApplication() as VibePlayerApp }

    single<Context> { androidApplication().applicationContext }

    single<CoroutineScope> {
        (androidApplication() as VibePlayerApp).applicationScope
    }

}