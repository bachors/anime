package com.bachors.anime

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

data class Genre(
    val name: String,
    val slug: String,
    val otakudesu_url: String
)

data class AnimeItem(
    val title: String,
    val slug: String,
    val japanese_title: String? = null,
    val poster: String,
    val rating: String? = null,
    val type: String? = null,
    val status: String? = null,
    val studio: String? = null,
    val genres: List<Genre>? = null,
    val synopsis: String? = null,
    val current_episode: String? = null,
    val release_day: String? = null,
    val newest_release_date: String? = null,
    val episode_count: String? = null,
    val last_release_date: String? = null,
    val otakudesu_url: String
)

data class SearchResponse(
    val status: String,
    val creator: String,
    val message: String,
    val search_results: List<AnimeItem>
)

data class HomeData(
    val ongoing_anime: List<AnimeItem>,
    val complete_anime: List<AnimeItem>
)

data class HomeResponse(
    val status: String,
    val creator: String,
    val data: HomeData
)

data class Episode(
    val episode: String,
    val episode_number: Int,
    val slug: String,
    val otakudesu_url: String
)

data class Batch(
    val slug: String,
    val otakudesu_url: String,
    val uploaded_at: String
)

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
    val recommendations: List<AnimeItem>
)

data class DetailResponse(
    val status: String,
    val creator: String,
    val data: AnimeDetail
)

data class EpisodeData(
    val episode: String,
    val stream_url: String
)

data class EpisodeResponse(
    val status: String,
    val creator: String,
    val data: EpisodeData
)

interface AnimeApi {
    @GET("anime/search/{keyword}")
    suspend fun searchAnime(@Path("keyword") keyword: String): SearchResponse
    
    @GET("anime/home")
    suspend fun getHomeData(): HomeResponse
    
    @GET("anime/anime/{slug}")
    suspend fun getAnimeDetail(@Path("slug") slug: String): DetailResponse
    
    @GET("anime/episode/{slug}")
    suspend fun getEpisodeData(@Path("slug") slug: String): EpisodeResponse
}

object ApiClient {
    private const val BASE_URL = "https://www.sankavollerei.com/"
    
    val api: AnimeApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AnimeApi::class.java)
    }
}