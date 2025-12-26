package com.example.vibeplayer.core.domain

import kotlinx.coroutines.flow.Flow

interface SettingsDataStore {

    suspend fun setDefaultDuration(duration: Int)
    fun getDefaultDuration(): Flow<Int>

    suspend fun setDefaultSize(size: Int)
    fun getDefaultSize(): Flow<Int>

}