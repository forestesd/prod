package com.example.apis.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apis.data.utils.docsMapperToArticle
import com.example.apis.domain.models.Article
import com.example.apis.domain.models.Docs
import com.example.apis.domain.models.NewsUi
import com.example.apis.domain.models.SearchType
import com.example.apis.domain.use_cases.GetFiltersUseCase
import com.example.apis.domain.use_cases.GetNewsPullToRefreshUseCase
import com.example.apis.domain.use_cases.GetNewsUseCase
import com.example.apis.domain.use_cases.GetSearchNewsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class NewsViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase,
    private val getNewsPullToRefreshUseCase: GetNewsPullToRefreshUseCase,
    private val getSearchNewsUseCase: GetSearchNewsUseCase,
    private val getFiltersUseCase: GetFiltersUseCase
) : ViewModel() {
    private val _news = MutableStateFlow(
        NewsUi(
            news = emptyList(),
            filters = emptyList(),
            selectedSection = "All"
        )
    )
    val news: StateFlow<NewsUi> = _news

    private var _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var _isMoreLoading = MutableStateFlow(true)
    val isMoreLoading: StateFlow<Boolean> = _isMoreLoading

    private var _searchNews = MutableStateFlow<List<Article>>(emptyList())
    val searchNews: StateFlow<List<Article>> = _searchNews

    private var _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching
    private var _searchType = MutableStateFlow(SearchType.News)
    val searchType: StateFlow<SearchType> = _searchType

    init {
        loadFilters()
    }

    fun changeSearchType(){
        _searchType.update { currentType -> currentType.toggle() }
    }

    private fun loadFilters() {
        viewModelScope.launch {
            _news.value.filters = getFiltersUseCase.invoke()
        }
    }

    fun onScroll(){
        _news.update { it.copy(page = _news.value.page+1) }
        loadMoreNews(_news.value.selectedSection)
    }

    fun changeSelectedFilter(filter: String) {
        if (filter == news.value.selectedSection) return

        loadNews(filter)
        _news.update { it.copy(selectedSection = filter) }
    }

    private fun validateSearchNews(newsList: List<Docs>): List<Article> {
        return newsList.map { newsItem ->
            docsMapperToArticle(newsItem)
        }
            .filter { it.abstract.isNotEmpty() && it.published_date.isNotEmpty() && it.title.isNotEmpty() }
    }


    fun loadNews(section: String = "All") {
        viewModelScope.launch {
            _isLoading.value = true

            val newsList =
                getNewsUseCase.invoke(
                    source = "nyt",
                    section = section,
                    apiKey = "zdriWPTRBqSbP75bHAG4LQY1atLj26Dg",
                    page = _news.value.page
                )

            _news.value.news = validateSearchNews(newsList)

            _isLoading.value = false


        }
    }

    private fun loadMoreNews(section: String = "All") {
        viewModelScope.launch {
            _isMoreLoading.value = true

            val newsList =
                getNewsUseCase.invoke(
                    source = "nyt",
                    section = section,
                    apiKey = "zdriWPTRBqSbP75bHAG4LQY1atLj26Dg",
                    page = _news.value.page
                )

            _news.value.news = validateSearchNews(newsList)

            _isMoreLoading.value = false


        }
    }

    fun loadNewsPullToRefresh() {
        viewModelScope.launch {
            _isLoading.value = true

            val newsList =
                getNewsPullToRefreshUseCase.invoke(
                    "nyt",
                    "world",
                    "zdriWPTRBqSbP75bHAG4LQY1atLj26Dg"
                ).sortedByDescending {
                    try {
                        val formattedDate = it.pub_date.replaceFirst(
                            "(\\d{2})(\\d{2})$".toRegex(), "$1:$2"
                        )
                        OffsetDateTime.parse(
                            formattedDate,
                            DateTimeFormatter.ISO_OFFSET_DATE_TIME
                        )
                    } catch (e: Exception) {
                        null
                    }
                }

            _news.value.news = validateSearchNews(newsList)

            _isLoading.value = false

        }
    }

    fun loadSearchNews(q: String) {
        if (q.length >= 2) {
            viewModelScope.launch {
                _isLoading.value = true
                val newsList = getSearchNewsUseCase.invoke(q, "zdriWPTRBqSbP75bHAG4LQY1atLj26Dg")
                    .sortedByDescending {
                        try {
                            val formattedDate = it.pub_date.replaceFirst(
                                "(\\d{2})(\\d{2})$".toRegex(), "$1:$2"
                            )
                            OffsetDateTime.parse(
                                formattedDate,
                                DateTimeFormatter.ISO_OFFSET_DATE_TIME
                            )
                        } catch (e: Exception) {
                            null
                        }
                    }
                _searchNews.value = validateSearchNews(newsList)
                _isLoading.value = false
            }
        }
    }

    fun setIsSearching(isSearching: Boolean) {
        _isSearching.value = isSearching
    }

    fun getImageUrlForArticle(article: Article): String? {
        return if (article.multimedia.isNotEmpty()) article.multimedia.find { it.width == 440f }?.url else null
    }
}