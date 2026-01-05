package com.example.vibeplayer.core.data

import com.example.vibeplayer.core.domain.SongRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SongScanner(
    private val songRepository: SongRepository
) {
    private val _isScanning = MutableStateFlow(false)
    val isScanning = _isScanning.asStateFlow()

    suspend fun scan(applyFilters: Boolean = false): Int {
        if (_isScanning.value) return 0

        _isScanning.value = true
        return try {
            val count = songRepository.syncSongs(applyFilters = applyFilters)
            count
        } finally {
            _isScanning.value = false
        }
    }
}