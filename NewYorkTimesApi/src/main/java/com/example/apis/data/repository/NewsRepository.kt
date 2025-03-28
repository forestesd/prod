package com.example.apis.data.repository

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import com.example.apis.R
import com.example.apis.data.TimesApiService
import com.example.apis.domain.models.Article
import com.example.apis.domain.models.CacheNews
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
    private var cachedNews = mutableStateOf<List<CacheNews>>(emptyList())
    private var lastUpdateTime = 0L
    private val cacheDuration = 6 * 60 * 60 * 1000L

    override suspend fun getNews(source: String, apiKey: String, section: String): List<Docs> {
        val currentTime = System.currentTimeMillis()

        if (
            (currentTime - lastUpdateTime) < cacheDuration
            && cachedNews.value.isNotEmpty()
            && cachedNews.value.any { it.section == section }
        ) {
            return cachedNews.value.find { it.section == section }!!.news
        }
        val filterQuery = when (section) {
            "All" -> "type_of_material:(\"News\")"
            "Technology" -> "news_desk:(\"Technology\" \"Science\" \"Tech\")"
            else -> "news_desk:(\"$section\") AND type_of_material:(\"News\")"
        }
        return try {
            val res = searchApiService.searchNews(
                filterQuery = filterQuery,
                apiKey = apiKey
            )

            cachedNews.value = if (cachedNews.value.any { it.section == section }) {
                cachedNews.value.map {
                    if (it.section == section) it.copy(news = res.response.docs) else it
                }
            } else {
                cachedNews.value + CacheNews(section, res.response.docs)
            }
            lastUpdateTime = System.currentTimeMillis()

            res.response.docs

        } catch (e: Exception) {
            cachedNews.value.flatMap { it.news }
        }
    }

    override suspend fun getNewsPullToRefresh(
        source: String,
        section: String,
        apiKey: String
    ): List<Docs> {

        val filterQuery = when (section) {
            "All" -> "type_of_material:(\"News\")"
            "Technology" -> "news_desk:(\"Technology\" \"Science\" \"Tech\")"
            else -> "news_desk:(\"$section\") AND type_of_material:(\"News\")"
        }

        return try {
            val response = withContext(Dispatchers.IO) {
                searchApiService.searchNews(
                    filterQuery = filterQuery,
                    apiKey = apiKey
                )
            }

            cachedNews.value = cachedNews.value.map {
                if (it.section == section) it.copy(news = response.response.docs) else it
            }
            lastUpdateTime = System.currentTimeMillis()

            response.response.docs

        } catch (e: Exception) {
            cachedNews.value.flatMap { it.news }
        }
    }

    override suspend fun getSearchNews(
        q: String,
        apiKey: String
    ): List<Docs> {
        var responseList: List<Docs> = emptyList()
        try {
            val res = searchApiService.searchNews(
                q = q,
                apiKey = apiKey
            )
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