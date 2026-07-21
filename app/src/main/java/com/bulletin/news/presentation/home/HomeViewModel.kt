package com.bulletin.news.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bulletin.news.domain.model.Article
import com.bulletin.news.domain.useCase.news.GetHeadlinesUseCase
import com.bulletin.news.domain.useCase.news.SearchNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHeadlinesUseCase: GetHeadlinesUseCase,
    private val searchNewsUseCase: SearchNewsUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    /**
     * چرا این‌جوری، نه یه HomeUiState معمولی؟
     * PagingData خودش snapshot و state داخلی داره (کدوم صفحه‌ها لود شدن، loadState و ...)
     * و قرار نیست دوباره توی یه StateFlow دیگه بسته‌بندی بشه؛ طبق مستندات رسمی Paging،
     * این Flow مستقیم با collectAsLazyPagingItems() توی Composable جمع‌آوری می‌شه.
     * debounce/distinctUntilChanged روی سرچ باعث می‌شه با هر حرف تایپ‌شده یه ریکوئست جدید نره،
     * و flatMapLatest یعنی اگه کاربر سریع category عوض کنه یا تایپ کنه، ریکوئست قبلی cancel می‌شه.
     */
    val articles: Flow<PagingData<Article>> =
        combine(
            _selectedCategory,
            _searchQuery.debounce(400.milliseconds).distinctUntilChanged()
        ) { category, query -> category to query }
            .flatMapLatest { (category, query) ->
                if (query.isBlank()) {
                    getHeadlinesUseCase(category)
                } else {
                    searchNewsUseCase(query)
                }
            }
            .cachedIn(viewModelScope)

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onCategorySelected(category: String?) {
        _selectedCategory.value = category
    }
}