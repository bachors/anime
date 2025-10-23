package com.bachors.anime

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {
    private val _animeDetail = MutableStateFlow<AnimeDetail?>(null)
    val animeDetail: StateFlow<AnimeDetail?> = _animeDetail
    
    suspend fun getAnimeDetail(slug: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.api.getAnimeDetail(slug)
                _animeDetail.value = response.data
            } catch (e: Exception) {
                _animeDetail.value = null
            }
        }
    }
}