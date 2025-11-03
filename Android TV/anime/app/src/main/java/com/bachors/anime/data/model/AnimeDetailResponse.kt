package com.bachors.anime.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AnimeDetailResponse(
    val status: String,
    val creator: String,
    val data: AnimeDetail
)

@Serializable
data class AnimeDetail(
    val title: String,
    val slug: String,
    val japanese_title: String,
    val poster: String,
    val rating: String,
    val produser: String,
    val type: String,
    val status: String,
    val episode_count: String,
    val duration: String,
    val release_date: String,
    val studio: String,
    val genres: List<Genre>,
    val synopsis: String,
    val batch: Batch?,
    val episode_lists: List<Episode>,
    val recommendations: List<Recommendation>
)

@Serializable
data class Batch(
    val slug: String,
    val otakudesu_url: String,
    val uploaded_at: String
)

@Serializable
data class Episode(
    val episode: String,
    val episode_number: Int,
    val slug: String,
    val otakudesu_url: String
)

@Serializable
data class Recommendation(
    val title: String,
    val slug: String,
    val poster: String,
    val otakudesu_url: String
)