package com.example.eccr_demo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.marsphotos.ui.screens.DemoViewModel

@Composable
fun SettingsScreen(
    viewModel: DemoViewModel,
) {


    Column() {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Enable location to show news from your country",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = viewModel.demoUiState.value.showLocationPermissionPrompt,
            onCheckedChange = {viewModel.onSwitchCheckedChange(it)},
            modifier = Modifier.padding(start = 16.dp)
        )
    }


   /*     if(viewModel.demoUiState.value.showEnableLocationFromSettingsNote)
        Text(
            text = "You previously denied access location permission, please turn it on manually from device settings",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )*/

    }


    if(viewModel.demoUiState.value.showLocationPermissionPrompt)
    LocationAccessScreen(
        viewModel, Modifier.fillMaxSize()
    )

}


@Composable
fun LocationAdDialog(
    onAccept:  () -> Unit,
    onDeny: () -> Unit
) {
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(text = "Privacy Policy",
                    onTextLayout = {  } )
            },
            text = {
                Text(
                    text = "Do you agree to use of location for personalized advertising",
                    onTextLayout = {  }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onAccept()
                        showDialog = false
                    }
                ) {
                    Text(text = "Accept",
                        onTextLayout = {  } )
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        onDeny()
                        showDialog = false
                    }
                ) {
                    Text(text = "Deny",
                        onTextLayout = {  } )
                }
            },
            properties = DialogProperties(dismissOnClickOutside = false)
        )
    }
}