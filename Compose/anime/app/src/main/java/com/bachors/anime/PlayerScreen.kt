package com.bachors.anime

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(slug: String, navController: NavHostController) {
    val viewModel: PlayerViewModel = viewModel()
    val episodeData by viewModel.episodeData.collectAsState()
    val scope = rememberCoroutineScope()
    
    LaunchedEffect(slug) {
        scope.launch {
            viewModel.getEpisodeData(slug)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Player") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        episodeData?.let { data ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Text(
                    text = data.episode,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
                
                AndroidView(
                    factory = { context ->
                        android.webkit.WebView(context).apply {
                            webViewClient = android.webkit.WebViewClient()
                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                            settings.mediaPlaybackRequiresUserGesture = false
                            loadUrl(data.stream_url)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}