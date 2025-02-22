package com.example.apis

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Named

class NewsRepository @Inject constructor(
    @Named("newsApi") val timesApiService: TimesApiService,
    @Named("searchApi") val  searchApiService: TimesApiService
) {
    private var cachedNews = mutableStateOf<List<Article>>(emptyList())
    private var lastUpdateTime = 0L
    private val cacheDuration = 6 * 60 * 60 * 1000L

    suspend fun getNews(source: String, section: String, apiKey: String): List<Article> {
        val currentTime = System.currentTimeMillis()

        if ((currentTime - lastUpdateTime) < cacheDuration && cachedNews.value.isNotEmpty()) {
            return cachedNews.value
        }

        return try {
            val response = withContext(Dispatchers.IO) {
                timesApiService.getNews(source, section, apiKey)
            }

            cachedNews.value = response.results
            lastUpdateTime = System.currentTimeMillis()

            response.results

        } catch (e: Exception) {
            cachedNews.value
        }
    }

    suspend fun getNewsPullToRefresh(source: String, section: String, apiKey: String): List<Article>{
        return try {
            val response = withContext(Dispatchers.IO) {
                timesApiService.getNews(source, section, apiKey)
            }

            cachedNews.value = response.results
            lastUpdateTime = System.currentTimeMillis()

            response.results

        } catch (e: Exception) {
            cachedNews.value
        }
    }

    suspend fun getSearchNews(
        q: String,
        apiKey: String
    ): List<Docs> {
        var responseList: List<Docs> = emptyList()
        try {
            val res = searchApiService.searchNews(q, apiKey)
           responseList = res.response.docs
        }catch (_:HttpException){

        }
        return responseList

    }


}