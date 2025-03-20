package com.example.apis.data.utils

import com.example.apis.domain.models.Article
import com.example.apis.domain.models.Docs
import com.example.apis.domain.models.Image
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun docsMapperToArticle(
    news: Docs
): Article {
    val multiMedia: List<Image> = listOf(
        Image(
            "https://static01.nyt.com/${news.multimedia.find { it.width == 600f }?.url}",
            440f
        )
    )
    val pubDate = try {
        val formattedDate = news.pub_date.replaceFirst(
            "(\\d{2})(\\d{2})$".toRegex(), "$1:$2"
        )
        LocalDate.parse(formattedDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    } catch (e: Exception) {
        news.pub_date
    }
    return Article(
        title = news.headline?.main.toString(),
        url = news.web_url,
        abstract = news.abstract,
        source = news.source.toString(),
        subsection = news.subsection_name.toString(),
        published_date = pubDate,
        multimedia = multiMedia
    )
}