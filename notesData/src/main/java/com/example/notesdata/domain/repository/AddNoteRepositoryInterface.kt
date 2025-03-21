package com.example.notesdata.domain.repository

import android.content.Context
import android.net.Uri
import com.example.notesdata.data.db.NewsEntity
import com.example.notesdata.data.db.TagEntity

interface AddNoteRepositoryInterface {
    suspend fun saveNote(
        context: Context,
        news: NewsEntity?,
        noteContent: String,
        selectedImage: List<Uri>,
        selectedTags: List<TagEntity>
    )

    suspend fun saveImageToAppDirectory(context: Context, imageUri: Uri): Uri?
}