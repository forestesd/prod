package com.example.notesdata.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "post")
data class PostEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "news_id") val newsId: Long? = null
)
@Entity(tableName = "news")
data class NewsEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "article_url") val articleUrl: String,
    @ColumnInfo(name = "title") val title: String
)

@Entity(tableName = "post_images", foreignKeys = [
    ForeignKey(entity = PostEntity::class,
        parentColumns = ["id"],
        childColumns = ["post_id"],
        onDelete = ForeignKey.CASCADE)
],
    indices = [Index(value = ["post_id"])]
)
data class PostImageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "post_id") val postId: Long,
    @ColumnInfo(name = "image_url") val photoUrl: String,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
)


@Entity(tableName = "tags")
data class TagEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String
)



@Entity(tableName = "post_tags", primaryKeys = ["post_id", "tag_id"], foreignKeys = [
    ForeignKey(entity = PostEntity::class,
        parentColumns = ["id"],
        childColumns = ["post_id"],
        onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = TagEntity::class,
        parentColumns = ["id"],
        childColumns = ["tag_id"],
        onDelete = ForeignKey.CASCADE)
],
    indices = [Index(value = ["post_id"]), Index(value = ["tag_id"])]
)
data class PostTagEntity(
    @ColumnInfo(name = "post_id") val postId: Long,
    @ColumnInfo(name = "tag_id") val tagId: Long
)