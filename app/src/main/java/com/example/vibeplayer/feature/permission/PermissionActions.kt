package com.example.vibeplayer.feature.permission

sealed interface PermissionActions {
    data object NavigateMainPage : PermissionActions
    data class ShowDialog(val showDialog: Boolean): PermissionActions
}