package com.example.vibeplayer.app.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.vibeplayer.core.playback.MiniPlayerRoot
import com.example.vibeplayer.core.presentation.designsystem.theme.buttonPrimary
import com.example.vibeplayer.core.presentation.designsystem.theme.textPrimary
import com.example.vibeplayer.core.presentation.util.ObserveAsEvents
import com.example.vibeplayer.core.presentation.util.SnackbarController
import com.example.vibeplayer.feature.main.MainScreenRoot
import com.example.vibeplayer.feature.main.components.MainAppBar
import com.example.vibeplayer.feature.nowplaying.NowPlayingScreenRoot
import com.example.vibeplayer.feature.nowplaying.components.NowPlayingTopBar
import com.example.vibeplayer.feature.permission.PermissionScreenRoot
import com.example.vibeplayer.feature.search.SearchScreenRoot
import com.example.vibeplayer.feature.settings.SettingsScreenRoot
import com.example.vibeplayer.feature.settings.component.SettingsTopBar
import kotlinx.coroutines.launch


@Composable
fun NavigationRoot(
    permissionGranted: Boolean
) {

    val firstScreen = if (permissionGranted) NavigationScreens.MainPage else NavigationScreens.Permission
    val backStack = rememberNavBackStack(firstScreen)

    val currentScreen = backStack.lastOrNull()
    var miniPlayerActive by rememberSaveable { mutableStateOf(false) }

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
        bottomBar = {
            AnimatedVisibility(
                visible = backStack.lastOrNull() is NavigationScreens.MainPage && miniPlayerActive,
                enter = slideInVertically { it },
                exit = slideOutVertically { it }
            ) {
                MiniPlayerRoot(openNowPlaying = {
                    backStack.add(NavigationScreens.NowPlaying)
                })
            }
        },
        topBar = {
            when (currentScreen) {
                is NavigationScreens.MainPage -> {
                    MainAppBar(
                        onSearchClicked = {
                            backStack.add(NavigationScreens.Search)
                        },
                        onSettingsClick = {
                            backStack.add(NavigationScreens.Settings)
                        })
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
                    MainScreenRoot(openNowPlaying = {
                        backStack.add(NavigationScreens.NowPlaying)
                    })
                }

                entry<NavigationScreens.NowPlaying>(
                    metadata = NavDisplay.transitionSpec {
                        slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(600)
                        ) togetherWith ExitTransition.KeepUntilTransitionsFinished
                    } + NavDisplay.popTransitionSpec {
                        EnterTransition.None togetherWith
                                slideOutVertically(
                                    targetOffsetY = { it },
                                    animationSpec = tween(600)
                                )
                    }
                ) {
                    miniPlayerActive = true
                    NowPlayingScreenRoot()
                }

                entry<NavigationScreens.Settings> {
                    SettingsScreenRoot(onBackClick = {
                        backStack.removeLastOrNull()
                    })
                }

                entry<NavigationScreens.Search> {
                    SearchScreenRoot(
                        onCancelClick = {
                            backStack.removeLastOrNull()
                        },
                        onPlaySong = {
                            backStack.add(NavigationScreens.NowPlaying)
                        })
                }

            }
        )
    }
}