package com.bulletin.news.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bulletin.news.core.utils.Resource
import com.bulletin.news.domain.useCase.news.GetHeadlinesUseCase
import com.bulletin.news.domain.useCase.news.SearchNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
@OptIn(FlowPreview::class)
class HomeViewModel @Inject constructor(
    private val getNewsUseCase: GetHeadlinesUseCase,
    private val searchNewsUseCase: SearchNewsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val searchQueryFlow = MutableStateFlow("")

    init {
        getNews()
        viewModelScope.launch {
            searchQueryFlow
                .debounce(400.milliseconds)
                .distinctUntilChanged()
                .collectLatest { query ->
                    if (query.isBlank()) getNews() else performSearch(query)
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchQueryFlow.value = query
    }

    fun getNews() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = getNewsUseCase.invoke()) {
                is Resource.Success -> {
                    _uiState.update { it.copy(news = result.data, isLoading = false) }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
                else -> {}
            }
        }
    }

    private suspend fun performSearch(query: String) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        when (val result = searchNewsUseCase.invoke(query)) {
            is Resource.Success -> {
                _uiState.update { it.copy(news = result.data, isLoading = false) }
            }
            is Resource.Error -> {
                _uiState.update { it.copy(isLoading = false, error = result.message) }
            }
            else -> {}
        }
    }
}