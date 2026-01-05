package com.example.vibeplayer.core.mapper

import com.example.vibeplayer.core.database.SongEntity
import com.example.vibeplayer.core.domain.model.Song

fun List<SongEntity>.toDomainList(): List<Song> {
    return this.map { it.toDomainModel() }
}

fun SongEntity.toDomainModel(): Song =
    Song(
        songId = id,
        mediaStoreId = mediaStoreId,
        title = title,
        artist = artist,
        durationMs = durationMs,
        sizeBytes = sizeBytes,
        albumId = albumId
    )

fun List<Song>.toEntityList(): List<SongEntity> {
    return this.map { it.toEntity() }
}

fun Song.toEntity(): SongEntity =
    SongEntity(
        mediaStoreId = mediaStoreId,
        title = title,
        artist = artist,
        durationMs = durationMs,
        sizeBytes = sizeBytes,
        albumId = albumId
    )