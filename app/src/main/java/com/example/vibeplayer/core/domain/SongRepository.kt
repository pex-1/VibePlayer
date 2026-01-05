package com.example.vibeplayer.core.domain

import com.example.vibeplayer.core.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface SongRepository {
    suspend fun syncSongsIfEmpty(): Boolean

    fun observeSongs(): Flow<List<Song>>

    suspend fun searchSongs(query: String): List<Song>

    fun getDefaultDuration(): Flow<Int>

    fun getDefaultSize(): Flow<Int>

    suspend fun setDefaultDuration(duration: Int)

    suspend fun setDefaultSize(size: Int)

    suspend fun syncOnAppStart(): Boolean


    suspend fun syncSongs(applyFilters: Boolean = false): Int

}
