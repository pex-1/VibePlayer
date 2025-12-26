package com.example.vibeplayer.app.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.vibeplayer.core.presentation.designsystem.theme.buttonPrimary
import com.example.vibeplayer.core.presentation.designsystem.theme.textPrimary
import com.example.vibeplayer.core.presentation.util.ObserveAsEvents
import com.example.vibeplayer.core.presentation.util.SnackbarController
import com.example.vibeplayer.feature.main.MainScreenRoot
import com.example.vibeplayer.feature.main.components.MainAppBar
import com.example.vibeplayer.feature.nowplaying.NowPlayingScreenRoot
import com.example.vibeplayer.feature.nowplaying.components.NowPlayingTopBar
import com.example.vibeplayer.feature.permission.PermissionScreenRoot
import com.example.vibeplayer.feature.settings.SettingsScreenRoot
import com.example.vibeplayer.feature.settings.component.SettingsTopBar
import kotlinx.coroutines.launch


@Composable
fun NavigationRoot(
    permissionGranted: Boolean
) {

    val backStack =
        rememberNavBackStack(if (permissionGranted) NavigationScreens.MainPage else NavigationScreens.Permission)

    val currentScreen = backStack.lastOrNull()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    ObserveAsEvents(
        SnackbarController.events,
        snackbarHostState
    ) { event ->
        coroutineScope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()

            val result = snackbarHostState.showSnackbar(
                message = event.message,
                actionLabel = event.action?.name,
                withDismissAction = event.onDismiss != null,
                duration = SnackbarDuration.Short
            )

            if (result == SnackbarResult.ActionPerformed) {
                event.action?.action?.invoke()
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = {
                    Snackbar(
                        it,
                        containerColor = MaterialTheme.colorScheme.buttonPrimary,
                        contentColor = MaterialTheme.colorScheme.textPrimary,
                        actionColor = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(10.dp)
                    )
                })
        },
        topBar = {
            when (currentScreen) {
                is NavigationScreens.MainPage -> {
                    MainAppBar {
                        backStack.add(NavigationScreens.Settings)
                    }
                }

                is NavigationScreens.Settings -> {
                    SettingsTopBar {
                        backStack.removeLastOrNull()
                    }
                }

                is NavigationScreens.NowPlaying -> {
                    NowPlayingTopBar {
                        backStack.removeLastOrNull()
                    }
                }

                else -> {}
            }
        }

    ) { paddingValues ->
        NavDisplay(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = entryProvider {

                entry<NavigationScreens.Permission> {
                    PermissionScreenRoot(onNavigateToMain = {
                        backStack.removeLastOrNull()
                        backStack.add(NavigationScreens.MainPage)
                    })
                }

                entry<NavigationScreens.MainPage> {
                    MainScreenRoot(playSongAction = {
                        backStack.add(NavigationScreens.NowPlaying(it))
                    })
                }

                entry<NavigationScreens.NowPlaying> { navEntry ->
                    NowPlayingScreenRoot(songId = navEntry.songId)
                }

                entry<NavigationScreens.Settings> {
                    SettingsScreenRoot(onBackClick = {
                        backStack.removeLastOrNull()
                    })
                }

            }
        )
    }
}