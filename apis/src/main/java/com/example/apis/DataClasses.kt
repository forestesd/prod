package com.example.apis


data class TimesResponse(
    val status: String,
    val results: List<Article>
)

data class Article(
    val title: String,
    val url: String,
    val published_date: String
)