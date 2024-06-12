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

@file:OptIn(ExperimentalMaterial3Api::class)


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eccr_demo.R
import com.example.marsphotos.ui.screens.DemoViewModel

@Composable
fun DemoApp() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = { DemoTopAppBar() }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize().padding(top = it.calculateTopPadding())
        ) {
            val demoViewModel: DemoViewModel =
                viewModel()
            HomeScreen(
                demoUiState = demoViewModel.demoUiState.value,
                retryAction = {},
                contentPadding = it,
                viewModel=demoViewModel
            )
        }
    }
}

@Composable
fun DemoTopAppBar( modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineSmall,

                onTextLayout = {  }
            )
        },
        modifier = modifier
    )
}
