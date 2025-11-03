package com.bachors.anime.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AnimeHomeResponse(
    val status: String,
    val creator: String,
    val data: HomeData
)

@Serializable
data class HomeData(
    val ongoing_anime: List<OngoingAnime>,
    val complete_anime: List<CompleteAnime>
)

@Serializable
data class OngoingAnime(
    val title: String,
    val slug: String,
    val poster: String,
    val current_episode: String,
    val release_day: String,
    val newest_release_date: String,
    val otakudesu_url: String
)

@Serializable
data class CompleteAnime(
    val title: String,
    val slug: String,
    val poster: String,
    val episode_count: String,
    val rating: String,
    val last_release_date: String,
    val otakudesu_url: String
)