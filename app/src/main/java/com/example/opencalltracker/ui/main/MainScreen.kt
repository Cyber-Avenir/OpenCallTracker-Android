package com.example.opencalltracker.ui.main

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MainScreen(modifier: Modifier = Modifier, viewModel: MainViewModel = viewModel(factory = MainViewModel.Factory)) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val roleRequestLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        viewModel.onDialerRoleRequestResult()
    }

    val toggleDetection: () -> Unit = {
        if (!uiState.isDetectionEnabled && !uiState.isDefaultDialer) {
            roleRequestLauncher.launch(createDialerRoleRequestIntent(context))
        } else {
            viewModel.onDetectionToggled()
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.refreshDialerRole()
    }

    MainScreenContent(uiState = uiState, onDetectionToggled = toggleDetection, modifier = modifier)
}
