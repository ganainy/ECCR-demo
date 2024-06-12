package com.example.eccr_demo.ui.screens;

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable;
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.marsphotos.ui.screens.DemoViewModel

import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.delay

@Composable
fun LocationAccessScreen(viewModel: DemoViewModel, modifier: Modifier) {
    if (!viewModel.demoUiState.value.enableLocation) {
        return
    }

    val TAG = "LocationAccessScreen"
    val context = LocalContext.current
    var location by remember { mutableStateOf("Your location") }

    // Create a permission launcher
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted: Boolean ->
                if (isGranted) {
                    // Permission granted, update the location
                    getCurrentLocation(context) { lat, long ->
                        viewModel.postLocation(lat, long)
                    }
                } else {
                    viewModel.postMacAndIp()
                }
            }
        )

    // Use LaunchedEffect to request permission only once after the launcher is ready
    LaunchedEffect(key1 = requestPermissionLauncher) {
        if (!hasLocationPermission(context)) {
            // Request location permission
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            // Permission already granted, update the location
            getCurrentLocation(context) { lat, long ->
                viewModel.postLocation(lat, long)
            }
        }
    }

            if (hasLocationPermission(context)) {
                // Permission already granted, update the location
                getCurrentLocation(context) { lat, long ->
                    viewModel.postLocation(lat,long)
                }
            } else {
                LaunchedEffect(key1 = Unit) { // Use Unit as the key to run only once
                    // Request location permission
                    delay(1000)
                    requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                }

            }

}



private fun hasLocationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

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
