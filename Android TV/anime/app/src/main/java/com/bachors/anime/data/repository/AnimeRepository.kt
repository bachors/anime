package com.bachors.anime.data.repository

import com.bachors.anime.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL

class AnimeRepository {
    private val json = Json { ignoreUnknownKeys = true }
    
    suspend fun getHomeAnime(): Result<AnimeHomeResponse> = withContext(Dispatchers.IO) {
        try {
            val url = URL("https://www.sankavollerei.com/anime/home")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val homeResponse = json.decodeFromString<AnimeHomeResponse>(response)
                Result.success(homeResponse)
            } else {
                Result.failure(Exception("HTTP Error: $responseCode"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getAnimeDetail(slug: String): Result<AnimeDetailResponse> = withContext(Dispatchers.IO) {
        try {
            val url = URL("https://www.sankavollerei.com/anime/anime/$slug")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val detailResponse = json.decodeFromString<AnimeDetailResponse>(response)
                Result.success(detailResponse)
            } else {
                Result.failure(Exception("HTTP Error: $responseCode"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun searchAnime(keyword: String): Result<AnimeSearchResponse> = withContext(Dispatchers.IO) {
        try {
            val url = URL("https://www.sankavollerei.com/anime/search/$keyword")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val animeResponse = json.decodeFromString<AnimeSearchResponse>(response)
                Result.success(animeResponse)
            } else {
                Result.failure(Exception("HTTP Error: $responseCode"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getEpisodeStream(slug: String): Result<EpisodeStreamResponse> = withContext(Dispatchers.IO) {
        try {
            val url = URL("https://www.sankavollerei.com/anime/episode/$slug")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val streamResponse = json.decodeFromString<EpisodeStreamResponse>(response)
                Result.success(streamResponse)
            } else {
                Result.failure(Exception("HTTP Error: $responseCode"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}