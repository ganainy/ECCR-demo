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
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.example.eccr_demo.R
import com.example.eccr_demo.data.FakeNews
import com.example.eccr_demo.data.ScreenType
import com.example.eccr_demo.data.fakeNewsList
import com.example.eccr_demo.ui.screens.NewsDetailScreen
import com.example.eccr_demo.ui.screens.SettingsScreen
import com.example.marsphotos.ui.screens.DemoUiState
import com.example.marsphotos.ui.screens.DemoViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    demoUiState: DemoUiState,
    retryAction: () -> Unit,
    viewModel: DemoViewModel,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {

    if (demoUiState.screenType==ScreenType.HomeScreen){


        if(!demoUiState.isPrivacyPolicyDismissed){
    PrivacyPolicyDialog({
        viewModel.updatePrivacyPolicyVisibilityState()
    }, {
        viewModel.updatePrivacyPolicyVisibilityState()
    })
        }

    NewsColumn(viewModel,modifier=modifier)
    }else if (demoUiState.screenType==ScreenType.Details){

        NewsDetailScreen(news = demoUiState.currentNews, viewModel =viewModel )
    }else if(demoUiState.screenType==ScreenType.Settings){
        SettingsScreen(viewModel)
    }


}

val TAG = "HomeScreen"


@Composable
fun NewsColumn(viewModel: DemoViewModel, modifier: Modifier) {
Box (modifier = modifier.fillMaxSize()){
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Iterate through the fake news list and create a card for each item
        fakeNewsList.forEach { news ->
            NewsCard(news = news, viewModel = viewModel)
        }

    }

    val adLink = viewModel.demoUiState.value.homeAdLink
    if (adLink == null) {
        Spacer(modifier = Modifier.height(0.dp))
    } else {
        AdBanner(imageUrl = adLink,modifier=modifier)
      }
}

}



@Composable
fun NewsCard(news: FakeNews,viewModel: DemoViewModel) {

    val  onClick: () -> Unit ={
        // Navigate to new screen to show full news content
       viewModel.navigateToDetails(news)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = news.title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                onTextLayout = {  }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = news.content,
                style = MaterialTheme.typography.bodyMedium,
                onTextLayout = {  }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "By ${news.author}",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray,
                onTextLayout = {  }
            )

        }
    }
}



@Composable
fun AdBanner(imageUrl: String, modifier: Modifier) {
    val x=1
    Box(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.BottomCenter)
            .systemBarsPadding()
    ) {
        val painter = rememberImagePainter(
            data = imageUrl,
            builder = {
                crossfade(true) // Optional: Enable crossfade animation between images
                error(R.drawable.ic_broken_image)// Fallback image if load fails

            }
        )
        // Check if the image is still loading
        if (painter.state is ImagePainter.State.Loading) {
            // Show a loading indicator while the image is loading
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Image(
            painter = painter,
            contentDescription = null, // Content description for accessibility
            modifier = modifier
                .height(100.dp) // Adjust size as needed
                .align(Alignment.BottomCenter),
            contentScale = ContentScale.FillWidth, // Crop the image if it's too large or small
            alignment = Alignment.BottomCenter // Align image to the bottom center of the Box
        )
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
        Text(text = stringResource(R.string.loading_failed),
            onTextLayout = {  } , modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry),
                onTextLayout = {  } )
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
                Text(text = "Privacy Policy",
                    onTextLayout = {  } )
            },
            text = {
                Text(
                    text = "By using this app, you agree to our Privacy Policy. " +
                            "Please read our Privacy Policy carefully before using the app.",
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