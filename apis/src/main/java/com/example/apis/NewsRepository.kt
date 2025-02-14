package com.example.apis

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import retrofit2.HttpException
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val api: TimesApiService
) {
    private var cachedNews = mutableStateOf<List<Article>>(emptyList())

    suspend fun getNews(
        source: String,
        section: String,
        apiKey: String
    ): List<Article> {

        return try {

            val response = api.getNews(source, section, apiKey)

            cachedNews.value = response.results
            response.results

        } catch (e: Exception) {
            Log.e("ERROR", "Error fetching news: ${e.message}")
            cachedNews.value
        }


    }

    fun getCachedNews(): List<Article>{
        return cachedNews.value
    }
}