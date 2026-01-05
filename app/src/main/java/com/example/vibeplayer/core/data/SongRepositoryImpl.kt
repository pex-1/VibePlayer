package com.example.vibeplayer.core.data

import com.example.vibeplayer.core.database.SongDao
import com.example.vibeplayer.core.database.toDomainList
import com.example.vibeplayer.core.database.toEntityList
import com.example.vibeplayer.core.domain.LocalSongProvider
import com.example.vibeplayer.core.domain.SettingsDataStore
import com.example.vibeplayer.core.domain.SongRepository
import com.example.vibeplayer.core.domain.model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SongRepositoryImpl(
    private val songDao: SongDao,
    private val settingsDataStore: SettingsDataStore,
    private val localSongProvider: LocalSongProvider
) : SongRepository {

    override fun observeSongs(): Flow<List<Song>> = songDao.observeSongs().map { it.toDomainList() }

    override suspend fun searchSongs(query: String): List<Song> =
        songDao.searchSongs(query).toDomainList()

    override fun getDefaultDuration(): Flow<Int> = settingsDataStore.getDefaultDuration()

    override fun getDefaultSize(): Flow<Int> = settingsDataStore.getDefaultSize()

    override suspend fun setDefaultDuration(duration: Int) {
        settingsDataStore.setDefaultDuration(duration)
    }

    override suspend fun setDefaultSize(size: Int) {
        settingsDataStore.setDefaultSize(size)
    }

    suspend fun cleanUpRemovedSongs() {
        val songs = songDao.getSongsList()
        if (songs.isNotEmpty()) {
            songs.forEach {
                if (!localSongProvider.songExists(it.mediaStoreId)) {
                    songDao.removeSong(it)
                }
            }
        }
    }

    override suspend fun syncOnAppStart(): Boolean {
        val isEmpty = songDao.getSongCount() == 0

        if (isEmpty) {
            syncSongs(applyFilters = false)
            return true
        } else {
            cleanUpRemovedSongs()
        }
        return false
    }

    override suspend fun syncSongs(applyFilters: Boolean): Int {
        val songsFromDevice = localSongProvider.getAllSongs()

        songDao.removeAllSongs()

        val finalSongs = if (applyFilters) {
            val minDuration = settingsDataStore.getDefaultDuration().first()
            val minSize = settingsDataStore.getDefaultSize().first()

            songsFromDevice.filter {
                it.durationSec >= minDuration && it.sizeKb >= minSize
            }
        } else {
            songsFromDevice
        }

        songDao.upsertAll(finalSongs.toEntityList())
        return finalSongs.size
    }

    override suspend fun syncSongsIfEmpty(): Boolean {
        return if (songDao.getSongCount() == 0) {
            syncSongs(applyFilters = false)
            true
        } else {
            false
        }
    }
}

