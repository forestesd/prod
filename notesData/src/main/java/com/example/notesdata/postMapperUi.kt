package com.example.notesdata

import com.example.notesdata.db.PostEntity
import com.example.notesdata.db.PostImageEntity
import com.example.notesdata.db.TagEntity

fun postMapperUi(
    post: PostEntity,
    image: List<PostImageEntity>,
    tags: List<TagEntity>
): PostUi {
    return PostUi(
        postId = post.id,
        content = post.content,
        images = image.map { it.photoUrl },
        tags = tags.map { it.name }
    )
}