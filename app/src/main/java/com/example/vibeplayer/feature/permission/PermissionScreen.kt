package com.example.vibeplayer.feature.permission

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vibeplayer.R
import com.example.vibeplayer.app.MainActivity
import com.example.vibeplayer.core.presentation.designsystem.buttons.VibePlayerPrimaryButton
import com.example.vibeplayer.core.presentation.designsystem.components.LifecycleEventListener
import com.example.vibeplayer.core.presentation.designsystem.components.VibePlayerDialog
import com.example.vibeplayer.core.presentation.designsystem.theme.LogoIcon
import com.example.vibeplayer.core.presentation.designsystem.theme.VibePlayerTheme
import com.example.vibeplayer.core.presentation.designsystem.theme.bodyMediumRegular
import org.koin.androidx.compose.koinViewModel

@Composable
fun PermissionScreenRoot(
    viewModel: PermissionViewModel = koinViewModel(),
    onNavigateToMain: () -> Unit
) {
    val state by viewModel.permissionState.collectAsStateWithLifecycle()

    PermissionScreen(state) { action ->
        when (action) {
            is PermissionActions.NavigateMainPage -> onNavigateToMain()
            else -> viewModel.onActions(action)
        }
    }
}

@Composable
fun PermissionScreen(
    state: PermissionState,
    onActions: (PermissionActions) -> Unit,
) {
    val context = LocalContext.current
    val activity = context as MainActivity

    val permission = activity.permission

    LifecycleEventListener { events ->
        when (events) {
            Lifecycle.Event.ON_RESUME -> {
                val hasGranted = activity.checkMediaPermission()
                if (hasGranted) {
                    onActions(PermissionActions.NavigateMainPage)
                }
            }

            else -> {}
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { hasGranted ->
            if (hasGranted) {
                onActions(PermissionActions.NavigateMainPage)
            } else {
                val permanentlyDenied =
                    !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
                if (permanentlyDenied) {
                    onActions(PermissionActions.ShowDialog(true))
                }
            }
        },
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .widthIn(max = 380.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(56.dp),
                imageVector = LogoIcon,
                contentDescription = stringResource(R.string.logo_icon)
            )

            Text(
                modifier = Modifier.padding(top = 20.dp),
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                modifier = Modifier.padding(top = 4.dp, bottom = 20.dp),
                text = stringResource(R.string.access_needed_string),
                style = MaterialTheme.typography.bodyMediumRegular,
                minLines = 2,
                textAlign = TextAlign.Center
            )

            VibePlayerPrimaryButton(
                text = stringResource(R.string.allow_access),
            ) {
                permissionLauncher.launch(permission)
            }
        }
        if (state.showDialog) {
            VibePlayerDialog(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.permission_denied),
                text = stringResource(R.string.dialog_message),
                confirmText = stringResource(R.string.try_again),
                dismissText = stringResource(R.string.ok),
                confirmButtonClick = {
                    onActions(PermissionActions.ShowDialog(false))
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.fromParts("package", context.packageName, null)
                    context.startActivity(intent)
                },
                dismissButtonClick = {
                    onActions(PermissionActions.ShowDialog(false))
                }
            )
        }
    }
}

@Preview
@Composable
private fun PermissionScreenPreview() {
    VibePlayerTheme {
        PermissionScreen(
            PermissionState(false),
        ) { }
    }
}