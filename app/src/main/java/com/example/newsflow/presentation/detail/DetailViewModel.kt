    package com.example.newsflow.presentation.detail

    import android.util.Log
    import androidx.lifecycle.SavedStateHandle
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.example.newsflow.domain.model.Article
    import com.example.newsflow.domain.useCase.news.BookmarkArticleUseCase
    import com.example.newsflow.domain.useCase.news.DeleteBookmarkUseCase
    import com.example.newsflow.domain.useCase.news.IsBookmarkedUseCase
    import com.google.gson.Gson
    import dagger.hilt.android.lifecycle.HiltViewModel
    import kotlinx.coroutines.flow.MutableStateFlow
    import kotlinx.coroutines.flow.StateFlow
    import kotlinx.coroutines.flow.asStateFlow
    import kotlinx.coroutines.flow.update
    import kotlinx.coroutines.launch
    import javax.inject.Inject

    @HiltViewModel
    class DetailViewModel @Inject constructor(
        savedStateHandle: SavedStateHandle,
        private val bookmarkUseCase: BookmarkArticleUseCase,
        private val deleteBookmarkUseCase: DeleteBookmarkUseCase
        ,private val isBookmarkedUseCase: IsBookmarkedUseCase
    ) : ViewModel() {

        private val articleJson = savedStateHandle.get<String>("article")
        private val article = Gson().fromJson(articleJson, Article::class.java)

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
                article?.let {
                    Log.d("DetailViewModel", "Article: ${it.title}")
                    Log.d("DetailViewModel", "isBookmarked: ${uiState.value.isBookmarked}")
                    if (uiState.value.isBookmarked) {
                        deleteBookmarkUseCase.invoke(it)
                    } else {
                Log.d("DetailViewModel", "Saving bookmark...")
                bookmarkUseCase.invoke(it)
                Log.d("DetailViewModel", "Bookmark saved!")
            }
                    _uiState.update { state -> state.copy(isBookmarked = !state.isBookmarked) }
                }
            }
        }
    }