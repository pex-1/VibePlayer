package com.example.vibeplayer.core.data

import com.example.vibeplayer.core.database.SongDao
import com.example.vibeplayer.core.database.toDomainList
import com.example.vibeplayer.core.database.toEntityList
import com.example.vibeplayer.core.domain.Result
import com.example.vibeplayer.core.domain.SettingsDataStore
import com.example.vibeplayer.core.domain.SongRepository
import com.example.vibeplayer.core.domain.model.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File

class SongRepositoryImpl(
    private val songDao: SongDao,
    private val settingsDataStore: SettingsDataStore,
    private val localSongProvider: LocalSongProvider
) : SongRepository {

    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    init {
        cleanUpRemovedSongs()
    }

    override fun getSongs(): Flow<List<Song>> {
        return songDao.getSongs().map { it.toDomainList() }
    }

    override suspend fun searchSongs(query: String): List<Song> {
        return songDao.searchSongs(query).toDomainList()
    }

    override fun getDefaultDuration(): Flow<Int> {
        return settingsDataStore.getDefaultDuration()
    }

    override fun getDefaultSize(): Flow<Int> {
        return settingsDataStore.getDefaultSize()
    }

    override suspend fun setDefaultDuration(duration: Int) {
        settingsDataStore.setDefaultDuration(duration)
    }

    override suspend fun setDefaultSize(size: Int) {
        settingsDataStore.setDefaultSize(size)
    }

    private fun cleanUpRemovedSongs() {
        repositoryScope.launch {
            val songs = songDao.getSongs().first()
            songs.map { songEntity ->
                if (!File(songEntity.filePath).exists()) {
                    songDao.removeSong(songEntity)
                }
            }
        }
    }

    override suspend fun syncSongsIfEmpty(): Flow<Result<Unit>> {
        if (songDao.getSongs().first().isEmpty()) {
            scanSongs()
            return flowOf(Result.Success(Unit))
        } else {
            return flowOf(Result.Success(Unit))
        }
    }

    override suspend fun scanSongs(applyFilters: Boolean): Int {
        val songsFromDevice = localSongProvider.getAllSongs()

        if (applyFilters) {
            songDao.removeAllSongs()
            val minDuration = settingsDataStore.getDefaultDuration().first()
            val minSize = settingsDataStore.getDefaultSize().first()
            val filteredSongs = songsFromDevice.filter {
                it.duration >= minDuration && it.size >= minSize
            }.toEntityList()
            songDao.upsertAll(filteredSongs)
        } else {
            songDao.upsertAll(songsFromDevice.toEntityList())
        }
        return songsFromDevice.size
    }
}

