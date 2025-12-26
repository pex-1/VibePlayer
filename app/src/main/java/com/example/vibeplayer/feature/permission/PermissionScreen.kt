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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.example.vibeplayer.core.presentation.designsystem.components.VibePLayerLifecycleEventListener
import com.example.vibeplayer.core.presentation.designsystem.components.VibePlayerDialog
import com.example.vibeplayer.core.presentation.designsystem.theme.VibePlayerIcons
import com.example.vibeplayer.core.presentation.designsystem.theme.VibePlayerTheme
import com.example.vibeplayer.core.presentation.designsystem.theme.bodyMediumRegular
import com.example.vibeplayer.core.presentation.designsystem.theme.textPrimary
import com.example.vibeplayer.core.presentation.designsystem.theme.textSecondary
import org.koin.androidx.compose.koinViewModel

@Composable
fun PermissionScreenRoot(
    viewModel: PermissionViewModel = koinViewModel(),
    onNavigateToMain: () -> Unit
) {
    val permissionUiState by viewModel.permissionUiState.collectAsStateWithLifecycle()

    PermissionScreen(permissionUiState) { action ->
        when(action) {
            is PermissionActions.NavigateMainPage -> onNavigateToMain()
            else -> viewModel.onActions(action)
        }
    }
}


@Composable
fun PermissionScreen(
    permissionUiState: PermissionUiState,
    onActions: (PermissionActions) -> Unit,
) {
    val context = LocalContext.current
    val activity = context as MainActivity

    val permission = activity.permission

    VibePLayerLifecycleEventListener { events ->
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
                //Check if permanently denied
                //this will set automatically true if the user deny permission twice since
                //it's handled internally by android studio
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
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(56.dp),
                imageVector = VibePlayerIcons.Logo,
                contentDescription = stringResource(R.string.logo_icon)
            )

            Text(
                modifier = Modifier.padding(top = 28.dp),
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = stringResource(R.string.access_needed_string),
                style = MaterialTheme.typography.bodyMediumRegular,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.padding(bottom = 16.dp))

            VibePlayerPrimaryButton (
                text = stringResource(R.string.allow_access),
            ) {
                permissionLauncher.launch(permission)
            }
        }
        if (permissionUiState.showDialog) {
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
            PermissionUiState(false),
        ) { }
    }
}