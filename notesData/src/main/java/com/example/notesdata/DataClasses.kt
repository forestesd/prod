package com.example.notesdata

data class PostUi(
    val postId: Long,
    val content: String,
    val images: List<String>,
    val tags: List<String>
)
