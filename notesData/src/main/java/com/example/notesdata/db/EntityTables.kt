package com.example.notesdata.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "post")
data class PostEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
)


@Entity(tableName = "post_images", foreignKeys = [
    ForeignKey(entity = PostEntity::class,
        parentColumns = ["id"],
        childColumns = ["event_id"],
        onDelete = ForeignKey.CASCADE)
])
data class PostImageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "event_id") val eventId: Long,
    @ColumnInfo(name = "photo_url") val photoUrl: String,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
)


@Entity(tableName = "tags")
data class Tag(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String
)



@Entity(tableName = "post_tags", primaryKeys = ["post_id", "tag_id"], foreignKeys = [
    ForeignKey(entity = PostEntity::class,
        parentColumns = ["id"],
        childColumns = ["post_id"],
        onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = Tag::class,
        parentColumns = ["id"],
        childColumns = ["tag_id"],
        onDelete = ForeignKey.CASCADE)
])
data class PostTagEntity(
    @ColumnInfo(name = "post_id") val eventId: Long,
    @ColumnInfo(name = "tag_id") val tagId: Long
)