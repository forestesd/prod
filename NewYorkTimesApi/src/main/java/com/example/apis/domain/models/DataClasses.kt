package com.example.apis.domain.models


data class TimesResponse(
    val status: String,
    val results: List<Article>,

    )

data class NewsUi(
    var news: List<Article>,
    var filters: List<String>,
    var selectedSection: String,
    val page: Int = 0
)

data class Article(
    val title: String,
    val url: String,
    val abstract: String,
    val source: String,
    val subsection: String,
    val published_date: String,
    val multimedia: List<Image>
)


data class Image(
    val url: String,
    val width: Float
)

data class SearchTimesResponse(
    val status: String,
    val response: ResponseDetails
)

data class ResponseDetails(
    val docs: List<Docs>
)

data class Docs(
    val headline: HeadLine?,
    val abstract: String,
    val source: String?,
    val pub_date: String,
    val web_url: String,
    val multimedia: List<Image>,
    val subsection_name: String?
)

data class HeadLine(
    val main: String?
)

data class CacheNews(
    val section: String,
    val pages: Map<Int, List<Docs>> = emptyMap()
)
enum class SearchType{
    News,
    Tickers;

    fun toggle(): SearchType = when (this) {
        News -> Tickers
        Tickers -> News
    }
}