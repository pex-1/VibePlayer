package com.example.vibeplayer.core.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {

    @Query("SELECT * FROM songentity ORDER BY title ASC")
    fun getSongs(): Flow<List<SongEntity>>

    @Query(
        "SELECT * FROM songentity " +
                "WHERE title LIKE '%' || :query || '%' OR artist LIKE '%' || :query || '%' " +
                "COLLATE NOCASE ORDER BY title ASC"
    )
    suspend fun searchSongs(query: String): List<SongEntity>

    @Upsert
    suspend fun upsertSong(song: SongEntity)

    @Delete
    suspend fun removeSong(song: SongEntity)

    @Query("DELETE FROM songentity")
    suspend fun removeAllSongs()

    @Upsert
    suspend fun upsertAll(songs: List<SongEntity>)
}