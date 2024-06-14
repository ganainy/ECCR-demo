package com.example.eccr_demo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.marsphotos.ui.screens.DemoViewModel

@Composable
fun SettingsScreen(
    viewModel: DemoViewModel,
) {
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
            checked = viewModel.demoUiState.value.enableLocation,
            onCheckedChange = {viewModel.enableLocation(it)},
            modifier = Modifier.padding(start = 16.dp)
        )
    }


    if(viewModel.demoUiState.value.enableLocation)
    LocationAccessScreen(
        viewModel, Modifier.fillMaxSize()
    )

}

