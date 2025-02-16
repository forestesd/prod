package com.example.apis

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class NewsViewModel @Inject constructor(
    private val repository: NewsRepository,
) : ViewModel() {
    private val _news = mutableStateOf<List<Article>>(emptyList())
    val news: State<List<Article>> = _news

    private var _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private var _searchNews = mutableStateOf<List<Docs>>(emptyList())
    val searchNews: State<List<Docs>> = _searchNews

    private var _isSearching = mutableStateOf(false)
    val isSearching: State<Boolean> = _isSearching


    private fun validateNews(newsList: List<Article>): List<Article> {
        return newsList.map { newsItem ->
            newsItem.copy(
                published_date = try {
                    LocalDate.parse(newsItem.published_date, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                        .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                } catch (e: Exception) {
                    newsItem.published_date
                }
            )
        }.filter { it.abstract.isNotEmpty() }
    }

    private fun validateSearchNews(newsList: List<Docs>): List<Docs> {
        return newsList.map { newsItem ->
            newsItem.copy(
                pub_date = try {
                    val formattedDate = newsItem.pub_date.replaceFirst(
                        "(\\d{2})(\\d{2})$".toRegex(), "$1:$2"
                    )
                    LocalDate.parse(formattedDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                        .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                } catch (e: Exception) {
                    newsItem.pub_date
                }
            )
        }.filter { it.abstract.isNotEmpty() }
    }


    fun loadNews() {
        viewModelScope.launch {
            if (_news.value.isEmpty()) {
                _isLoading.value = true

                val newsList =
                    repository.getNews("nyt", "world", "zdriWPTRBqSbP75bHAG4LQY1atLj26Dg")
                _news.value = validateNews(newsList)

                _isLoading.value = false
            } else {
                val newsList = repository.getCachedNews()
                _news.value = validateNews(newsList)
                _isLoading.value = false
            }

        }
    }

    fun loadSearchNews(q: String) {
        if (q.length >= 3) {
            viewModelScope.launch {
                val newsList = repository.getSearchNews(q, "zdriWPTRBqSbP75bHAG4LQY1atLj26Dg").sortedByDescending {
                    try {
                        val formattedDate = it.pub_date.replaceFirst(
                            "(\\d{2})(\\d{2})$".toRegex(), "$1:$2"
                        )
                        OffsetDateTime.parse(formattedDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                    } catch (e: Exception) {
                        null
                    }
                }
                _searchNews.value = validateSearchNews(newsList)
            }
        }
    }

    fun setIsSearching(isSearching: Boolean) {
        _isSearching.value = isSearching
    }

    fun getImageUrlForDocs(docs: Docs): String? {
        return if (docs.multimedia.isNotEmpty()) "https://static01.nyt.com/${docs.multimedia.find { it.width == 600f }?.url}" else null
    }

    fun getImageUrlForArticle(article: Article): String? {
        return if (article.multimedia.isNotEmpty()) article.multimedia.find { it.width == 440f }?.url else null
    }
}