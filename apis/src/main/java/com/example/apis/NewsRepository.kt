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
        val url = "https://api.nytimes.com/svc/news/v3/content/$source/$section.json?api-key=$apiKey"
        Log.i("INFO", "Request URL: $url")
        try {
            val response = api.getNews(source, section, apiKey)
            Log.i("INFO", "Response: ${response.results}")
            cachedNews = response.results
            return response.results
        } catch (e: HttpException) {
            Log.e("ERROR", "HTTP Error: ${e.response()?.errorBody()?.string()}")
        } catch (e: Exception) {
            Log.e("ERROR", "Error fetching news: ${e.message}")
            throw e
        }

        return emptyList()
    }
}