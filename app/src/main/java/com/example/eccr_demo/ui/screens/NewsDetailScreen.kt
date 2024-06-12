package com.example.eccr_demo.ui.screens

import AdBanner
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.eccr_demo.data.FakeNews
import com.example.marsphotos.ui.screens.DemoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailScreen(news: FakeNews,viewModel: DemoViewModel) {

    val demoUiState = viewModel.demoUiState.value
    val lorem="Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse convallis nisl ante, congue bibendum justo semper vitae. Duis non fermentum nisl. Etiam mattis, ipsum vel sagittis blandit, erat eros fringilla quam, at malesuada turpis erat id felis. Maecenas scelerisque leo eu sodales ornare. Duis at dictum quam. Nullam posuere mi in dolor commodo auctor. Proin non condimentum urna, vitae iaculis urna. Nullam efficitur sapien quis libero tincidunt, at fringilla tortor volutpat. Nulla maximus commodo mattis. Nam non blandit libero, at auctor sem. Phasellus pellentesque, massa ac pulvinar volutpat, erat nisi tempor purus, at scelerisque libero enim at mauris. Morbi eu est justo. Sed fermentum odio et ultricies blandit. Donec egestas vitae nisl vel posuere. Maecenas porttitor porttitor massa, quis facilisis erat iaculis nec. Ut venenatis ultrices massa."
    Box(modifier = Modifier.fillMaxSize())
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = news.title,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = lorem,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "By ${news.author}",
                style = MaterialTheme.typography.labelLarge,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth()
            )
        }





    val adLink = viewModel.demoUiState.value.detailsAdLink
    if (adLink == null) {
        Spacer(modifier = Modifier.height(0.dp))
    } else {
        AdBanner(imageUrl = adLink,modifier=Modifier)
    }


}