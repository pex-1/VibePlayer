package com.example.vibeplayer.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibeplayer.core.data.SongScanner
import com.example.vibeplayer.core.domain.SongRepository
import com.example.vibeplayer.core.presentation.util.SnackbarController
import com.example.vibeplayer.core.presentation.util.SnackbarEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val songRepository: SongRepository,
    private val songScanner: SongScanner
) : ViewModel() {

    private val _event = Channel<SettingsEvents>()
    val event = _event.receiveAsFlow()

    val state = combine(
        songRepository.getDefaultDuration(),
        songRepository.getDefaultSize(),
        songScanner.isScanning
    ) { duration, size, isScanning ->
        SettingsState(
            duration = DurationEnum.fromDuration(duration),
            size = SizeEnum.fromSize(size),
            isScanning = isScanning
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = SettingsState(
            isScanning = false,
            duration = DurationEnum.SECONDS_30,
            size = SizeEnum.KB_100
        )
    )

    fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.OnBackClickAction -> {}
            is SettingsAction.OnScanAction -> {
                viewModelScope.launch {
                    val count = songScanner.scan(true)
                    SnackbarController.sendEvent(
                        SnackbarEvent(
                            message = "Scan complete - $count songs found."
                        )
                    )
                    _event.send(SettingsEvents.OnNavigateBack)
                }
            }

            is SettingsAction.OnDurationChange -> {
                viewModelScope.launch {
                    songRepository.setDefaultDuration(action.selected.duration)
                }
            }

            is SettingsAction.OnSizeChange -> {
                viewModelScope.launch {
                    songRepository.setDefaultSize(action.selected.size)
                }
            }
        }
    }

}