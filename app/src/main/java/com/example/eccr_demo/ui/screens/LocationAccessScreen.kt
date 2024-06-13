package com.example.eccr_demo.ui.screens;

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable;
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.marsphotos.ui.screens.DemoViewModel

@Composable
fun LocationAccessScreen(viewModel: DemoViewModel, modifier: Modifier = Modifier) {
    if (!viewModel.demoUiState.value.showLocationPermissionPrompt) {
        return
    }

    val context = LocalContext.current

    // Create a permission launcher
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted: Boolean ->
                viewModel.demoUiState.value.isLocationGranted = isGranted
            }
        )

    // Use LaunchedEffect to request permission only once after the launcher is ready
    LaunchedEffect(key1 = Unit) {
        if (!hasLocationPermission(context)) {
            // Request location permission
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            // Permission already granted
        viewModel.demoUiState.value.isLocationGranted = true
        }
    }
}

// Helper function to check if the location permission is granted
fun hasLocationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

