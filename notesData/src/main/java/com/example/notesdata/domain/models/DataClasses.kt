package com.example.notesdata.domain.models

data class PostUi(
    val postId: Long,
    val content: String,
    val images: List<String>,
    val tags: List<String>,
    val isFavorite: Boolean = false,
    val news: NewsPostUi? = null
    )

data class NewsPostUi(
    val imageUrl: String,
    val newsUrl: String,
    val title: String
)
