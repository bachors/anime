package com.bachors.anime.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bachors.anime.data.model.*
import com.bachors.anime.data.repository.AnimeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val repository = AnimeRepository()
    
    private val _searchResults = MutableStateFlow<List<AnimeItem>>(emptyList())
    val searchResults: StateFlow<List<AnimeItem>> = _searchResults
    
    private val _ongoingAnime = MutableStateFlow<List<OngoingAnime>>(emptyList())
    val ongoingAnime: StateFlow<List<OngoingAnime>> = _ongoingAnime
    
    private val _completeAnime = MutableStateFlow<List<CompleteAnime>>(emptyList())
    val completeAnime: StateFlow<List<CompleteAnime>> = _completeAnime
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _isSearchMode = MutableStateFlow(false)
    val isSearchMode: StateFlow<Boolean> = _isSearchMode
    
    private val _animeDetail = MutableStateFlow<AnimeDetail?>(null)
    val animeDetail: StateFlow<AnimeDetail?> = _animeDetail
    
    private val _showDetail = MutableStateFlow(false)
    val showDetail: StateFlow<Boolean> = _showDetail
    
    private val _streamUrl = MutableStateFlow<String?>(null)
    val streamUrl: StateFlow<String?> = _streamUrl
    
    private val _showPlayer = MutableStateFlow(false)
    val showPlayer: StateFlow<Boolean> = _showPlayer
    
    init {
        loadHomeData()
    }
    
    private fun loadHomeData() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getHomeAnime()
                .onSuccess { response ->
                    _ongoingAnime.value = response.data.ongoing_anime
                    _completeAnime.value = response.data.complete_anime
                }
            _isLoading.value = false
        }
    }
    
    fun searchAnime(keyword: String) {
        if (keyword.isBlank()) {
            _isSearchMode.value = false
            _searchResults.value = emptyList()
            return
        }
        
        _isSearchMode.value = true
        viewModelScope.launch {
            _isLoading.value = true
            repository.searchAnime(keyword)
                .onSuccess { response ->
                    _searchResults.value = response.data
                }
                .onFailure {
                    _searchResults.value = emptyList()
                }
            _isLoading.value = false
        }
    }
    
    fun getAnimeDetail(slug: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getAnimeDetail(slug)
                .onSuccess { response ->
                    _animeDetail.value = response.data
                    _showDetail.value = true
                }
            _isLoading.value = false
        }
    }
    
    fun hideDetail() {
        _showDetail.value = false
        _animeDetail.value = null
    }
    
    fun playEpisode(slug: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getEpisodeStream(slug)
                .onSuccess { response ->
                    _streamUrl.value = response.data.stream_url
                    _showPlayer.value = true
                }
            _isLoading.value = false
        }
    }
    
    fun hidePlayer() {
        _showPlayer.value = false
        _streamUrl.value = null
    }
}