package com.example.vibeplayer.core.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {

    @Query("SELECT * FROM songs ORDER BY title ASC")
    fun observeSongs(): Flow<List<SongEntity>>

    @Query(
        "SELECT * FROM songs " +
                "WHERE title LIKE '%' || :query || '%' OR artist LIKE '%' || :query || '%' " +
                "COLLATE NOCASE ORDER BY title ASC"
    )
    suspend fun searchSongs(query: String): List<SongEntity>

    @Query("SELECT * FROM songs ORDER BY title ASC")
    suspend fun getSongsList(): List<SongEntity>

    @Query("SELECT COUNT(*) FROM songs")
    suspend fun getSongCount(): Int

    @Query("DELETE FROM songs WHERE mediaStoreId NOT IN (:existingIds)")
    suspend fun deleteMissing(existingIds: Set<Long>)

    @Query("DELETE FROM songs WHERE mediaStoreId IN (:mediaStoreIds)")
    suspend fun deleteByMediaStoreIds(mediaStoreIds: Set<Long>)

    @Query("UPDATE songs SET mediaStoreId = :newId WHERE id = :songId")
    suspend fun updateMediaStoreId(songId: Long, newId: Long)

    @Upsert
    suspend fun upsertSong(song: SongEntity)

    @Delete
    suspend fun removeSong(song: SongEntity)

    @Query("DELETE FROM songs")
    suspend fun removeAllSongs()

    @Upsert
    suspend fun upsertAll(songs: List<SongEntity>)
}