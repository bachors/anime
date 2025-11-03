package com.bachors.anime.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AnimeSearchResponse(
    val status: String,
    val creator: String,
    val data: List<AnimeItem>
)

@Serializable
data class AnimeItem(
    val title: String,
    val slug: String,
    val poster: String,
    val genres: List<Genre>,
    val status: String,
    val rating: String,
    val url: String
)

@Serializable
data class Genre(
    val name: String,
    val slug: String,
    val otakudesu_url: String
)