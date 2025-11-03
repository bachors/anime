package com.bachors.anime.data.model

import kotlinx.serialization.Serializable

@Serializable
data class EpisodeStreamResponse(
    val status: String,
    val creator: String,
    val data: EpisodeStreamData
)

@Serializable
data class EpisodeStreamData(
    val episode: String,
    val stream_url: String
)