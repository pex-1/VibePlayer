package com.example.vibeplayer.core.data.mediastore

import android.content.ContentResolver
import android.content.ContentUris
import android.provider.MediaStore
import com.example.vibeplayer.core.domain.LocalSongProvider
import com.example.vibeplayer.core.domain.model.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalSongProviderImpl(private val contentResolver: ContentResolver) : LocalSongProvider {

    companion object {
        private val PROJECTION = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.ALBUM_ID
        )

        private const val SELECTION = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

        private const val SORT_ORDER = "${MediaStore.Audio.Media.TITLE} ASC"
    }

    override suspend fun getAllSongs(): List<Song> = withContext(Dispatchers.IO) {
        val songs = mutableListOf<Song>()

        contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            PROJECTION,
            SELECTION,
            null,
            SORT_ORDER
        )?.use { cursor ->

            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val durationCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val sizeCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
            val albumIdCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

            while (cursor.moveToNext()) {
                songs.add(
                    Song(
                        songId = 0L,
                        mediaStoreId = cursor.getLong(idCol),
                        title = cursor.getString(titleCol) ?: "Unknown",
                        artist = cursor.getString(artistCol) ?: "Unknown",
                        durationMs = cursor.getLong(durationCol),
                        sizeBytes = cursor.getLong(sizeCol),
                        albumId = cursor.getLong(albumIdCol)
                    )
                )
            }
        }
        songs
    }

    override suspend fun songExists(mediaStoreId: Long): Boolean {
        val uri = ContentUris.withAppendedId(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            mediaStoreId
        )

        return try {
            contentResolver.openFileDescriptor(uri, "r")?.use { true } ?: false
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun findMovedSong(oldSong: Song): Long? =
        withContext(Dispatchers.IO) {

            val selection = """
            ${MediaStore.Audio.Media.DURATION} = ? AND
            ${MediaStore.Audio.Media.SIZE} = ? AND
            ${MediaStore.Audio.Media.IS_MUSIC} != 0
        """.trimIndent()

            val args = arrayOf(
                oldSong.durationMs.toString(),
                oldSong.sizeBytes.toString()
            )

            contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                arrayOf(
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ARTIST
                ),
                selection,
                args,
                null
            )?.use { cursor ->

                val idCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val titleCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                val artistCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)

                while (cursor.moveToNext()) {
                    val title = cursor.getString(titleCol)
                    val artist = cursor.getString(artistCol)

                    if (title.equals(oldSong.title, ignoreCase = true) &&
                        artist.equals(oldSong.artist, ignoreCase = true)
                    ) {
                        return@withContext cursor.getLong(idCol)
                    }
                }
            }

            null
        }
}