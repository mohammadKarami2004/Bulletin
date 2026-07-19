package com.bulletin.news.presentation.bookmark

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bulletin.news.domain.model.Article
import com.bulletin.news.domain.useCase.news.DeleteBookmarkUseCase
import com.bulletin.news.domain.useCase.news.GetBookmarksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    getBookmarksUseCase: GetBookmarksUseCase,
   private val deleteBookmarkUseCase: DeleteBookmarkUseCase
) : ViewModel()
{
    private val _uiState = MutableStateFlow(BookmarkUiState())
    val uiState: StateFlow<BookmarkUiState> = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(isLoading = true, error = null) }
         getBookmarksUseCase.invoke()
            .onEach { articles ->
                Log.d("BookmarkViewModel", "Articles: ${articles.size}")
                _uiState.update { it.copy(news = articles, isLoading = false) }
            }.launchIn(viewModelScope)


    }

    fun deleteBookmark(article: Article) {
        viewModelScope.launch {
            deleteBookmarkUseCase.invoke(article)
        }
    }
}