package com.example.apis.data.repository

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import com.example.apis.R
import com.example.apis.data.TimesApiService
import com.example.apis.domain.models.Article
import com.example.apis.domain.models.Docs
import com.example.apis.domain.repository.NewsRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import retrofit2.HttpException
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Named

class NewsRepository @Inject constructor(
    @Named("newsApi") val timesApiService: TimesApiService,
    @Named("searchApi") val searchApiService: TimesApiService,
    private val context: Context
) : NewsRepositoryInterface {
    private var cachedNews = mutableStateOf<List<Article>>(emptyList())
    private var lastUpdateTime = 0L
    private val cacheDuration = 6 * 60 * 60 * 1000L

    override suspend fun getNews(source: String, section: String, apiKey: String): List<Article> {
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

    override suspend fun getNewsPullToRefresh(
        source: String,
        section: String,
        apiKey: String
    ): List<Article> {
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

    override suspend fun getSearchNews(
        q: String,
        apiKey: String
    ): List<Docs> {
        var responseList: List<Docs> = emptyList()
        try {
            val res = searchApiService.searchNews(q, apiKey)
            responseList = res.response.docs
        } catch (_: HttpException) {

        }
        return responseList

    }

    override suspend fun getFilters(): List<String> {
        return loadFiltersFromJSON()
    }

    private fun loadFiltersFromJSON(): List<String> {
        val jsonFile = context.resources.openRawResource(R.raw.filters)
        val reader = InputStreamReader(jsonFile)
        val jsonStr = reader.readText()

        val jsonArray = JSONArray(jsonStr)
        val tickersList = mutableListOf<String>()
        for (i in 0..<jsonArray.length()) {
            tickersList.add(jsonArray.getString(i))
        }
        return tickersList
    }
}