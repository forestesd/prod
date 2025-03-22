package com.example.notesdata.domain.repository

import com.example.notesdata.data.db.PostEntity
import com.example.notesdata.data.db.TagEntity
import com.example.notesdata.domain.models.PostUi
import kotlinx.coroutines.flow.Flow

interface NoteRepositoryInterface {
    suspend fun getAllTags(): List<TagEntity>

    suspend fun checkFavorite(id: Long): PostEntity?

    suspend fun toggleFavorite(id: Long, isFavorite: Boolean)

    suspend fun getAllNotes(): Flow<List<PostUi>>
}