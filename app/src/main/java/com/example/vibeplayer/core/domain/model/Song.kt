package com.example.vibeplayer.core.domain.model

import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore

data class Song(
    val songId: Long = 0L,
    val mediaStoreId: Long = 0L,
    val title: String = "",
    val artist: String = "",
    val durationMs: Long = 0,
    val sizeBytes: Long = 0,
    val albumId: Long = 0L,
) {
    val contentUri: Uri
        get() = ContentUris.withAppendedId(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            mediaStoreId
        )

    val albumArtUri: Uri
        get() = ContentUris.withAppendedId(
            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
            albumId
        )

    val durationSec = durationMs/1000
    val sizeKb = sizeBytes/1024
}