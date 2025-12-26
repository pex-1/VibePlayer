package com.example.vibeplayer.feature.permission

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PermissionViewModel() : ViewModel() {
    private val _permissionUiState = MutableStateFlow(PermissionUiState())
    val permissionUiState = _permissionUiState.asStateFlow()

    fun onActions(permissionActions: PermissionActions) {
        when (permissionActions) {
            is PermissionActions.NavigateMainPage -> {}
            is PermissionActions.ShowDialog -> showDialog(permissionActions.showDialog)
        }
    }

    private fun showDialog(showDialog: Boolean){
        _permissionUiState.update { newState->
            newState.copy(showDialog = showDialog)
        }
    }
}