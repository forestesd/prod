package com.example.notesdata

import com.example.notesdata.db.NewsEntity
import com.example.notesdata.db.PostEntity
import com.example.notesdata.db.PostImageEntity
import com.example.notesdata.db.TagEntity

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