package com.bulletin.news.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.bulletin.news.domain.model.Article
import com.bulletin.news.domain.useCase.news.BookmarkArticleUseCase
import com.bulletin.news.domain.useCase.news.DeleteBookmarkUseCase
import com.bulletin.news.domain.useCase.news.IsBookmarkedUseCase
import com.bulletin.news.presentation.navigation.Screen
import com.bulletin.news.presentation.navigation.serializableNavType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val bookmarkUseCase: BookmarkArticleUseCase,
    private val deleteBookmarkUseCase: DeleteBookmarkUseCase,
    private val isBookmarkedUseCase: IsBookmarkedUseCase
) : ViewModel() {

    private val article = savedStateHandle.toRoute<Screen.DetailScreen>(
        typeMap = mapOf(typeOf<Article>() to serializableNavType<Article>())
    ).article
    private val _uiState = MutableStateFlow(DetailUiState(article = article))
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val isBookmarked = isBookmarkedUseCase.invoke(article.url)
            _uiState.update { it.copy(isBookmarked = isBookmarked) }
        }
    }

    fun toggleBookmark() {
        viewModelScope.launch {
            if (uiState.value.isBookmarked) {
                deleteBookmarkUseCase.invoke(article)
            } else {
                bookmarkUseCase.invoke(article)
            }
            _uiState.update { it.copy(isBookmarked = !it.isBookmarked) }
        }
    }
}
