package com.example.apis

import android.util.Log
import retrofit2.HttpException
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val api: TimesApiService
) {
    private var cachedNews: List<Article>? = null

    suspend fun getNews(
        source: String,
        section: String,
        apiKey: String
    ): List<Article> {

        try {
            val response = api.getNews(source, section, apiKey)
            Log.i("INFO", "Response: ${response.results}")
            cachedNews = response.results
            return response.results
        }catch (e: Exception) {
            Log.e("ERROR", "Error fetching news: ${e.message}")
            throw e
        }

    }
}