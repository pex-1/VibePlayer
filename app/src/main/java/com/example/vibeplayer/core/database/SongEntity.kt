package com.example.vibeplayer.core.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "songs",
    indices = [Index(value = ["mediaStoreId"], unique = true)]
)
data class SongEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "mediaStoreId")
    val mediaStoreId: Long,

    val title: String,
    val artist: String,
    val durationMs: Long,
    val sizeBytes: Long,
    val albumId: Long
)
