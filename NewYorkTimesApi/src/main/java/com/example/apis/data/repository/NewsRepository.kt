package com.example.apis.data.repository

import android.content.Context
import android.util.Log
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
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Named

class NewsRepository @Inject constructor(
    @Named("searchApi") val searchApiService: TimesApiService,
    private val context: Context
) : NewsRepositoryInterface {
    private var cachedNews = mutableStateOf<List<CacheNews>>(emptyList())
    private var lastUpdateTime = 0L
    private val cacheDuration = 6 * 60 * 60 * 1000L

    override suspend fun getNews(
        source: String,
        apiKey: String,
        section: String,
        page: Int
    ): List<Docs> {
        val currentTime = System.currentTimeMillis()

        if (
            (currentTime - lastUpdateTime) < cacheDuration &&
            cachedNews.value.any { it.section == section } &&
            cachedNews.value.find { it.section == section }?.pages?.containsKey(page) == true
        ) {
            return cachedNews.value.find { it.section == section }!!.pages[page]!!
        }
        val filterQuery = when (section) {
            "All" -> "type_of_material:(\"News\")"
            "Technology" -> "news_desk:(\"Technology\" \"Science\" \"Tech\")"
            else -> "news_desk:(\"$section\") AND type_of_material:(\"News\")"
        }
        return try {
            val res = searchApiService.searchNews(
                filterQuery = filterQuery,
                apiKey = apiKey,
                page = page
            ).response.docs.sortedByDescending {
                try {
                    val formattedDate = it.pub_date.replaceFirst(
                        "(\\d{2})(\\d{2})$".toRegex(), "$1:$2"
                    )
                    OffsetDateTime.parse(
                        formattedDate,
                        DateTimeFormatter.ISO_OFFSET_DATE_TIME
                    )
                } catch (e: Exception) {
                    null
                }
            }

            cachedNews.value = if (cachedNews.value.any { it.section == section }) {
                cachedNews.value.map {
                    if (it.section == section) {
                        it.copy(pages = it.pages + (page to res))
                    } else {
                        it
                    }
                }
            } else {
                cachedNews.value + CacheNews(section, mapOf(page to res))
            }

            lastUpdateTime = System.currentTimeMillis()

            val a = cachedNews.value
                .find { it.section == section }!!
                .pages
                .toSortedMap()
                .flatMap { it.value }

            a
        } catch (e: Exception) {
            cachedNews.value
                .find { it.section == section }
                ?.pages
                ?.toSortedMap()
                ?.flatMap { it.value }
                ?: emptyList()
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
                    apiKey = apiKey,
                    page = 0
                )
            }

            cachedNews.value = if (cachedNews.value.any { it.section == section }) {
                cachedNews.value.map {
                    if (it.section == section) {
                        it.copy(pages = mapOf(0 to response.response.docs) + it.pages.filterKeys { it != 0 })
                    } else {
                        it
                    }
                }
            } else {
                cachedNews.value + CacheNews(section, mapOf(0 to response.response.docs))
            }

            lastUpdateTime = System.currentTimeMillis()
            response.response.docs

        } catch (e: Exception) {
            cachedNews.value
                .find { it.section == section }
                ?.pages
                ?.get(0)
                ?: emptyList()
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