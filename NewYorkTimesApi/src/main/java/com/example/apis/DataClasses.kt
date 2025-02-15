package com.example.apis


data class TimesResponse(
    val status: String,
    val results: List<Article>,

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