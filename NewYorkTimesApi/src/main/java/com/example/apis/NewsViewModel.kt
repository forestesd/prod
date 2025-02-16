package com.example.apis

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {
    private val _news = mutableStateOf<List<Article>>(emptyList())
    val news: State<List<Article>> = _news

    private var _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading


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


    fun loadNews() {
        viewModelScope.launch {
            if (_news.value.isEmpty()) {
                _isLoading.value = true

                val newsList = repository.getNews("nyt", "world", "zdriWPTRBqSbP75bHAG4LQY1atLj26Dg")
                _news.value = validateNews(newsList)

                _isLoading.value =false
            }else{
                val newsList = repository.getCachedNews()
                _news.value = validateNews(newsList)
                _isLoading.value = false
            }

        }
    }

    fun getImageUrlForArticle(article: Article): String? {
        return if (article.multimedia.isNotEmpty()) article.multimedia.find { it.width == 440f }?.url else null
    }
}



