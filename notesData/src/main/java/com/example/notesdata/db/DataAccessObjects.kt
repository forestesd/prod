package com.example.notesdata.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PostDao {
    @Insert
    suspend fun insertPost(post: PostEntity): Long

    @Update
    suspend fun updatePost(post: PostEntity)

    @Delete
    suspend fun deletePost(post: PostEntity)

    @Query("SELECT * FROM post WHERE id = :id LIMIT 1")
    suspend fun getPostById(id: Long): PostEntity?

    @Query("SELECT * FROM post ORDER BY created_at DESC")
    suspend fun getAllPosts(): List<PostEntity>
}

@Dao
interface PostImageDao {
    @Insert
    suspend fun insertImage(photo: PostImageEntity)

    @Query("SELECT * FROM post_images WHERE post_id = :postId")
    suspend fun getImageByEventId(postId: Long): List<PostImageEntity>

}

@Dao
interface NewsDao{
    @Insert
    suspend fun insertNews(news: NewsEntity)

    @Query("SELECT *  FROM news WHERE article_url = :url")
    suspend fun getNewsByUrl(url: String): NewsEntity

    @Query("SELECT * FROM news WHERE id = :newsId")
    suspend fun getNewsById(newsId: Long): NewsEntity
}

@Dao
interface TagDao {

    @Insert
    suspend fun insertTags(tagsEntity: List<TagEntity>)

    @Query("SELECT * FROM tags")
    suspend fun getAllTags(): List<TagEntity>

    @Query("SELECT * FROM tags WHERE name = :tagName")
    suspend fun getTagByName(tagName: String): TagEntity
    @Query("SELECT * FROM tags WHERE id = :tagId")
    suspend fun getTagById(tagId: Long): TagEntity
}

@Dao
interface PostTagDao {
    @Insert
    suspend fun insertPostTag(eventTag: PostTagEntity)

    @Query("SELECT * FROM post_tags WHERE post_id = :postId")
    suspend fun getTagsForPost(postId: Long): List<PostTagEntity>

    @Query("DELETE FROM post_tags WHERE post_id = :postId AND tag_id = :tagId")
    suspend fun removeTagFromPost(postId: Long, tagId: Long)
}
