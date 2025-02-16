package com.example.apis

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import javax.inject.Inject
import javax.inject.Named

class NewsRepository @Inject constructor(
    @Named("newsApi") val timesApiService: TimesApiService,
    @Named("searchApi") val  searchApiService: TimesApiService
) {
    private var cachedNews = mutableStateOf<List<Article>>(emptyList())

    suspend fun getNews(
        source: String,
        section: String,
        apiKey: String
    ): List<Article> {

        return try {

            val response = timesApiService.getNews(source, section, apiKey)

            cachedNews.value = response.results
            response.results

        } catch (e: Exception) {
            Log.e("ERROR", "Error fetching news: ${e.message}")
            cachedNews.value
        }


    }

    suspend fun getSearchNews(
        q: String,
        apiKey: String
    ): List<Docs> {
        val response = searchApiService.searchNews(q, apiKey)
        return response.response.docs
    }

    fun getCachedNews(): List<Article>{
        return cachedNews.value
    }
}