package com.example.apis

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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

    fun loadNews() {
        viewModelScope.launch {
            Log.i("INFO", "Calling getNews()")
            repository.getNews("nyt", "world", "zdriWPTRBqSbP75bHAG4LQY1atLj26Dg").let { newsList ->
                _news.value = newsList.map { newsItem ->
                    newsItem.copy(
                        published_date = try {
                            LocalDate.parse(newsItem.published_date, DateTimeFormatter.ISO_OFFSET_DATE_TIME).format( DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                        }catch (e: Exception){
                            newsItem.published_date
                        }
                    )
                }.filter { it.abstract != "" }
            }
        }
    }

    fun getImageUrlForArticle(article: Article): String? {
        return if (article.multimedia.isNotEmpty()) article.multimedia.find { it.width == 440f }?.url else null
    }
}

class NewsViewModelFactory(private val repository: NewsRepository) : ViewModelProvider.Factory {
   override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

