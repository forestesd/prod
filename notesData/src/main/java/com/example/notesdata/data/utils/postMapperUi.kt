package com.example.notesdata.data.utils

import com.example.notesdata.domain.models.NewsPostUi
import com.example.notesdata.domain.models.PostUi
import com.example.notesdata.data.db.NewsEntity
import com.example.notesdata.data.db.PostEntity
import com.example.notesdata.data.db.PostImageEntity
import com.example.notesdata.data.db.TagEntity

fun postMapperUi(
    post: PostEntity,
    image: List<PostImageEntity>,
    tags: List<TagEntity>,
    news: NewsEntity? = null
): PostUi {
    return PostUi(
        postId = post.id,
        content = post.content,
        images = image.map { it.photoUrl },
        tags = tags.map { it.name },
        isFavorite = post.ifFavorites,
        news = if (news != null) NewsPostUi(
            imageUrl = news.imageUrl,
            newsUrl = news.articleUrl,
            title = news.title
        ) else null

    )
}