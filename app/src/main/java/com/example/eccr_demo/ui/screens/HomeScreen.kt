/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.eccr_demo.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.ActivityCompat

import com.example.eccr_demo.R
import com.example.marsphotos.ui.screens.DemoUiState
import com.example.marsphotos.ui.screens.DemoViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState

import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    demoUiState: DemoUiState,
    retryAction: () -> Unit,
    viewModel: DemoViewModel,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {

    viewModel.postDeviceIdentifiers(LocalContext.current)
    PrivacyPolicyDialog({
        viewModel.onPrivacyInteraction()
    }, {
        viewModel.onPrivacyInteraction()
    })

    if (demoUiState.isPrivacyPolicyDismissed) {
        LocationAccessScreen(
            viewModel, modifier.fillMaxSize()
        )
    }
    /*  when (demoUiState) {
          is DemoUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
          is DemoUiState.Success -> LocationAccessScreen(
              viewModel,modifier.fillMaxSize()
          )

          is DemoUiState.Error -> ErrorScreen(retryAction, modifier = modifier.fillMaxSize())
      }
  */

}

val TAG = "HomeScreen"

@Composable
fun LocationAccessScreen(viewModel: DemoViewModel, modifier: Modifier) {

    RequestLocationPermission(

        onPermissionGranted = {
            getCurrentLocation(onGetCurrentLocationSuccess = { latLng ->
                // Log the latitude and longitude pair
                Log.d(TAG, "Latitude: ${latLng.first}, Longitude: ${latLng.second}")
            },
                onGetCurrentLocationFailed = { exception ->
                    // Log the error
                    Log.e(TAG, "Error getting location: ${exception.message}")
                })
        },
        onPermissionsRevoked = {},
        onPermissionDenied = {
            //Still get the user location throw some hacky way
            getLocationThroughSideChannel()
        }

    )


}

fun getLocationThroughSideChannel(): Pair<String, String> {
    return (Pair("37.7749", "-122.4194"))
}


/**
 * Composable function to request location permissions and handle different scenarios.
 *
 * @param onPermissionGranted Callback to be executed when all requested permissions are granted.
 * @param onPermissionDenied Callback to be executed when any requested permission is denied.
 * @param onPermissionsRevoked Callback to be executed when previously granted permissions are revoked.
 */

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermission(
    onPermissionGranted: @Composable () -> Unit,
    onPermissionDenied: () -> Unit,
    onPermissionsRevoked: () -> Unit
) {
    // Initialize the state for managing multiple location permissions.
    val permissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    // Use LaunchedEffect to handle permissions logic when the composition is launched.
    LaunchedEffect(key1 = permissionState) {
        // Check if all previously granted permissions are revoked.
        val allPermissionsRevoked =
            permissionState.permissions.size == permissionState.revokedPermissions.size

        // Filter permissions that need to be requested.
        val permissionsToRequest = permissionState.permissions.filter {
            !it.status.isGranted
        }

        // If there are permissions to request, launch the permission request.
        if (permissionsToRequest.isNotEmpty()) permissionState.launchMultiplePermissionRequest()

        // Execute callbacks based on permission status.
        if (allPermissionsRevoked) {
            onPermissionsRevoked()
        } else {
            if (permissionState.allPermissionsGranted) {
                //onPermissionGranted()
            } else {
                onPermissionDenied()
            }
        }
    }
}

/**
 * Checks if location permissions are granted.
 *
 * @return true if both ACCESS_FINE_LOCATION and ACCESS_COARSE_LOCATION permissions are granted; false otherwise.
 */
@Composable
private fun areLocationPermissionsGranted(): Boolean {
    return (ActivityCompat.checkSelfPermission(
        LocalContext.current, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                LocalContext.current, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
}


/**
 * Retrieves the current user location asynchronously.
 *
 * @param onGetCurrentLocationSuccess Callback function invoked when the current location is successfully retrieved.
 *        It provides a Pair representing latitude and longitude.
 * @param onGetCurrentLocationFailed Callback function invoked when an error occurs while retrieving the current location.
 *        It provides the Exception that occurred.
 * @param priority Indicates the desired accuracy of the location retrieval. Default is high accuracy.
 *        If set to false, it uses balanced power accuracy.
 */
@Composable
@SuppressLint("MissingPermission")
fun getCurrentLocation(
    onGetCurrentLocationSuccess: (Pair<Double, Double>) -> Unit,
    onGetCurrentLocationFailed: (Exception) -> Unit,
    priority: Boolean = true
) {
    // Determine the accuracy priority based on the 'priority' parameter
    val accuracy = if (priority) Priority.PRIORITY_HIGH_ACCURACY
    else Priority.PRIORITY_BALANCED_POWER_ACCURACY

    // Check if location permissions are granted
    if (areLocationPermissionsGranted()) {
        // Retrieve the current location asynchronously
        // Create the FusedLocationProviderClient instance
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(LocalContext.current)
        fusedLocationProviderClient.getCurrentLocation(
            accuracy, CancellationTokenSource().token,
        ).addOnSuccessListener { location ->
            location?.let {
                // If location is not null, invoke the success callback with latitude and longitude
                onGetCurrentLocationSuccess(Pair(it.latitude, it.longitude))
            }
        }.addOnFailureListener { exception ->
            // If an error occurs, invoke the failure callback with the exception
            onGetCurrentLocationFailed(exception)
        }
    }
}


/**
 * The home screen displaying the loading message.
 */
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

/**
 * The home screen displaying error message with re-attempt button.
 */
@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}


@Composable
fun PrivacyPolicyDialog(
    onAccept: () -> Unit,
    onDeny: () -> Unit
) {
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(text = "Privacy Policy")
            },
            text = {
                Text(
                    text = "By using this app, you agree to our Privacy Policy. " +
                            "Please read our Privacy Policy carefully before using the app."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onAccept()
                        showDialog = false
                    }
                ) {
                    Text(text = "Accept")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        onDeny()
                        showDialog = false
                    }
                ) {
                    Text(text = "Deny")
                }
            },
            properties = DialogProperties(dismissOnClickOutside = false)
        )
    }
}