package com.bachors.anime

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlayerViewModel : ViewModel() {
    private val _episodeData = MutableStateFlow<EpisodeData?>(null)
    val episodeData: StateFlow<EpisodeData?> = _episodeData
    
    suspend fun getEpisodeData(slug: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.api.getEpisodeData(slug)
                _episodeData.value = response.data
            } catch (e: Exception) {
                _episodeData.value = null
            }
        }
    }
}