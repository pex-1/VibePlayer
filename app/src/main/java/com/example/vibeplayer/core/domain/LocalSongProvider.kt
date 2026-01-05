package com.example.vibeplayer.core.domain

import com.example.vibeplayer.core.domain.model.Song

interface LocalSongProvider {
    suspend fun getAllSongs(): List<Song>
    suspend fun songExists(mediaStoreId: Long): Boolean
}