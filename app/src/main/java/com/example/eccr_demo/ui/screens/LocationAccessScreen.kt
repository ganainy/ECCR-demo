package com.example.eccr_demo.ui.screens;

import android.Manifest
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

import com.google.android.gms.location.LocationServices

@Composable
fun LocationAccessScreen(viewModel: DemoViewModel, modifier: Modifier = Modifier) {
    if (!viewModel.demoUiState.value.enableLocation) {
        return
    }

    val context = LocalContext.current

    // Create a permission launcher
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            viewModel.selectDetailsAdType(isGranted)
        }
    )

    // Use LaunchedEffect to request permission only once after the launcher is ready
    LaunchedEffect(Unit) {
        if (!hasLocationPermission(context)) {
            // Request location permission
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            // Permission already granted, update the location
            viewModel.selectDetailsAdType(true)
        }
    }
}

// Helper function to check if the location permission is granted
private fun hasLocationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

// Function to get the current location
private fun getCurrentLocation(context: Context, callback: (Double, Double) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.lastLocation
        .addOnSuccessListener { location ->
            if (location != null) {
                val lat = location.latitude
                val long = location.longitude
                callback(lat, long)
            }
        }
        .addOnFailureListener { exception ->
            // Handle location retrieval failure
            exception.printStackTrace()
        }
}

