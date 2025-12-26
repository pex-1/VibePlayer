package com.example.vibeplayer.core.database

import com.example.vibeplayer.core.domain.model.Song

fun List<SongEntity>.toDomainList(): List<Song> {
    return this.map { it.toDomainModel() }
}

fun SongEntity.toDomainModel(): Song{
    return Song(
        id = this.id,
        title = this.title,
        artist = this.artist,
        filePath = this.filePath,
        duration = this.duration,
        size = this.size,
        embeddedArt = this.embeddedArt
    )
}

fun List<Song>.toEntityList(): List<SongEntity> {
    return this.map { it.toEntity() }
}

fun Song.toEntity(): SongEntity {
    return SongEntity(
        id = this.id,
        title = this.title,
        artist = this.artist,
        filePath = this.filePath,
        duration = this.duration,
        size = this.size,
        embeddedArt = this.embeddedArt
    )
}