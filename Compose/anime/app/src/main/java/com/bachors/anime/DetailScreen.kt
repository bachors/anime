package com.bachors.anime

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(slug: String, navController: NavHostController) {
    val viewModel: DetailViewModel = viewModel()
    val animeDetail by viewModel.animeDetail.collectAsState()
    val scope = rememberCoroutineScope()
    
    LaunchedEffect(slug) {
        scope.launch {
            viewModel.getAnimeDetail(slug)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        animeDetail?.let { detail ->
            LazyColumn(
                modifier = Modifier.padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Row {
                        AsyncImage(
                            model = detail.poster,
                            contentDescription = detail.title,
                            modifier = Modifier
                                .size(150.dp, 220.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = detail.title,
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Text(
                                text = detail.japanese_title,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "⭐ ${detail.rating}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "${detail.type} • ${detail.status}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "${detail.episode_count} Episodes",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = detail.studio,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
                item {
                    Text(
                        text = "Synopsis",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = detail.synopsis,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                item {
                    Text(
                        text = "Episodes",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                items(detail.episode_lists) { episode ->
                    Card(
                        modifier = Modifier.clickable {
                            navController.navigate("player/${episode.slug}")
                        }
                    ) {
                        Text(
                            text = episode.episode,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}