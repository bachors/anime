package com.bachors.anime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.key.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import coil.compose.AsyncImage
import com.bachors.anime.data.model.*
import com.bachors.anime.ui.theme.AnimeTheme
import com.bachors.anime.viewmodel.SearchViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RectangleShape
                ) {
                    AnimeSearchScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeSearchScreen(viewModel: SearchViewModel = viewModel()) {
    var searchQuery by remember { mutableStateOf("") }
    val searchResults by viewModel.searchResults.collectAsState()
    val ongoingAnime by viewModel.ongoingAnime.collectAsState()
    val completeAnime by viewModel.completeAnime.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSearchMode by viewModel.isSearchMode.collectAsState()
    val animeDetail by viewModel.animeDetail.collectAsState()
    val showDetail by viewModel.showDetail.collectAsState()
    val streamUrl by viewModel.streamUrl.collectAsState()
    val showPlayer by viewModel.showPlayer.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    if (showPlayer && streamUrl != null) {
        VideoPlayerScreen(
            streamUrl = streamUrl!!,
            onBack = { viewModel.hidePlayer() }
        )
        return
    }

    if (isLoading && showDetail) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.8f)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = Color.Blue)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Loading Episode...", color = Color.White)
            }
        }
        return
    }

    if (showDetail && animeDetail != null) {
        AnimeDetailScreen(
            anime = animeDetail!!,
            onBack = { viewModel.hideDetail() },
            onEpisodeClick = { slug -> viewModel.playEpisode(slug) }
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    if (it.isEmpty()) {
                        viewModel.searchAnime("")
                    }
                },
                label = { Text("Cari Anime...", color = Color.White) },
                modifier = Modifier
                    .weight(1f)
                    .onKeyEvent { keyEvent ->
                        if (keyEvent.key == Key.Enter && keyEvent.type == KeyEventType.KeyDown) {
                            viewModel.searchAnime(searchQuery)
                            keyboardController?.hide()
                            true
                        } else false
                    },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        viewModel.searchAnime(searchQuery)
                        keyboardController?.hide()
                    }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.Blue,
                    unfocusedBorderColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.width(16.dp))

            val uriHandler = androidx.compose.ui.platform.LocalUriHandler.current
            Icon(
                painter = androidx.compose.ui.res.painterResource(id = R.drawable.ic_github),
                contentDescription = "GitHub",
                tint = Color.White,
                modifier = Modifier
                    .size(48.dp)
                    .clickable {
                        uriHandler.openUri("https://github.com/bachors/anime")
                    }
                    .padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Blue)
            }
        } else {
            LazyColumn {
                if (isSearchMode) {
                    items(searchResults) { anime ->
                        SearchAnimeItem(
                            anime = anime,
                            onClick = { viewModel.getAnimeDetail(anime.slug) }
                        )
                    }
                } else {
                    item {
                        Text(
                            text = "Anime Ongoing",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        LazyRow {
                            items(ongoingAnime) { anime ->
                                OngoingAnimeItem(
                                    anime = anime,
                                    onClick = { viewModel.getAnimeDetail(anime.slug) }
                                )
                            }
                        }
                    }
                    item {
                        Text(
                            text = "Anime Complete",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        LazyRow {
                            items(completeAnime) { anime ->
                                CompleteAnimeItem(
                                    anime = anime,
                                    onClick = { viewModel.getAnimeDetail(anime.slug) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchAnimeItem(anime: AnimeItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
    ) {
        Row(
            modifier = Modifier.padding(12.dp)
        ) {
            AsyncImage(
                model = anime.poster,
                contentDescription = anime.title,
                modifier = Modifier
                    .width(80.dp)
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = anime.title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Status: ${anime.status}",
                    color = Color.Gray,
                    fontSize = 12.sp
                )

                Text(
                    text = "Rating: ${anime.rating}",
                    color = Color.Gray,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = anime.genres.joinToString(", ") { it.name },
                    color = Color.LightGray,
                    fontSize = 11.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun OngoingAnimeItem(anime: OngoingAnime, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .padding(horizontal = 4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            AsyncImage(
                model = anime.poster,
                contentDescription = anime.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = anime.title,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = anime.current_episode,
                color = Color.Green,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun CompleteAnimeItem(anime: CompleteAnime, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .padding(horizontal = 4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            AsyncImage(
                model = anime.poster,
                contentDescription = anime.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = anime.title,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "${anime.episode_count} Eps",
                color = Color.Blue,
                fontSize = 10.sp
            )

            Text(
                text = "★ ${anime.rating}",
                color = Color.Yellow,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun AnimeDetailScreen(anime: AnimeDetail, onBack: () -> Unit, onEpisodeClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
        ) {
            Text("Kembali", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            item {
                Row {
                    AsyncImage(
                        model = anime.poster,
                        contentDescription = anime.title,
                        modifier = Modifier
                            .width(200.dp)
                            .height(300.dp),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = anime.title,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = anime.japanese_title,
                            color = Color.Gray,
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "⭐ ${anime.rating}",
                            color = Color.Yellow,
                            fontSize = 14.sp
                        )

                        Text(
                            text = "${anime.episode_count} Episode • ${anime.duration}",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )

                        Text(
                            text = "Studio: ${anime.studio}",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )

                        Text(
                            text = anime.genres.joinToString(", ") { it.name },
                            color = Color.LightGray,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Sinopsis",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = anime.synopsis,
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Episode List",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            items(anime.episode_lists) { episode ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                        .clickable { onEpisodeClick(episode.slug) },
                    colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
                ) {
                    Text(
                        text = episode.episode,
                        color = Color.White,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}