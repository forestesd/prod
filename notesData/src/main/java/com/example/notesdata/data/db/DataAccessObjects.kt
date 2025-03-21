package com.example.notesdata.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

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
     fun getAllPosts(): Flow<List<PostEntity>>

    @Query("SELECT * FROM post WHERE is_favorites != null")
    suspend fun getFavoritesPosts(): PostEntity

    @Query("UPDATE post SET is_favorites = :isFavorite WHERE id = :id")
    suspend fun updatePostFavoriteStatus(id: Long, isFavorite: Boolean)

    @Transaction
    suspend fun insertPostWithImagesAndTags(post: PostEntity, images: List<PostImageEntity>, tags: List<PostTagEntity>): Long {

        val postId = insertPost(post)
        images.forEach { it.postId = postId }
        tags.forEach { it.postId = postId }

        insertImages(images)
        insertTags(tags)

        return postId
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImages(images: List<PostImageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTags(tags: List<PostTagEntity>)
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
