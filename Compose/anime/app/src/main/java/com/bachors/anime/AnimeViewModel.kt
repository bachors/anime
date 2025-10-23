package com.bachors.anime

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AnimeViewModel : ViewModel() {
    private val _ongoingAnime = MutableStateFlow<List<AnimeItem>>(emptyList())
    val ongoingAnime: StateFlow<List<AnimeItem>> = _ongoingAnime
    
    private val _completeAnime = MutableStateFlow<List<AnimeItem>>(emptyList())
    val completeAnime: StateFlow<List<AnimeItem>> = _completeAnime
    
    private val _searchResults = MutableStateFlow<List<AnimeItem>>(emptyList())
    val searchResults: StateFlow<List<AnimeItem>> = _searchResults
    
    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching
    
    init {
        loadHomeData()
    }
    
    private fun loadHomeData() {
        viewModelScope.launch {
            try {
                val response = ApiClient.api.getHomeData()
                _ongoingAnime.value = response.data.ongoing_anime
                _completeAnime.value = response.data.complete_anime
            } catch (e: Exception) {
                _ongoingAnime.value = emptyList()
                _completeAnime.value = emptyList()
            }
        }
    }
    
    suspend fun searchAnime(keyword: String) {
        try {
            _isSearching.value = true
            val response = ApiClient.api.searchAnime(keyword)
            _searchResults.value = response.search_results
        } catch (e: Exception) {
            _searchResults.value = emptyList()
        }
    }
    
    fun clearSearch() {
        _isSearching.value = false
        _searchResults.value = emptyList()
    }
}