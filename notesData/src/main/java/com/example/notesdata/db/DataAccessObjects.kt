package com.example.notesdata.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PostDao {
    @Insert
    suspend fun insertPost(post: PostEntity)

    @Update
    suspend fun updatePost(post: PostEntity)

    @Delete
    suspend fun deletePost(post: PostEntity)

    @Query("SELECT * FROM post WHERE id = :id LIMIT 1")
    suspend fun getPostById(id: Long): PostEntity?
}

@Dao
interface PostImageDao {
    @Insert
    suspend fun insertPhoto(photo: PostImageEntity)

    @Query("SELECT * FROM post_images WHERE event_id = :eventId")
    suspend fun getPhotosByEventId(eventId: Long): List<PostImageEntity>
}

@Dao
interface TagDao {
    @Insert
    suspend fun insertTag(tag: Tag)

    @Query("SELECT * FROM tags")
    suspend fun getAllTags(): List<Tag>

    @Query("SELECT * FROM tags WHERE id IN (:tagIds)")
    suspend fun getTagsByIds(tagIds: List<Long>): List<Tag>
}

@Dao
interface PostTagDao {
    @Insert
    suspend fun insertEventTag(eventTag: PostTagEntity)

    @Query("SELECT * FROM post_tags WHERE post_id = :eventId")
    suspend fun getTagsForEvent(eventId: Long): List<PostTagEntity>

    @Query("DELETE FROM post_tags WHERE post_id = :eventId AND tag_id = :tagId")
    suspend fun removeTagFromEvent(eventId: Long, tagId: Long)
}
