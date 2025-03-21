package com.example.notesdata.domain.use_cases

import android.content.Context
import android.net.Uri
import com.example.notesdata.data.db.NewsEntity
import com.example.notesdata.data.db.TagEntity
import com.example.notesdata.domain.repository.AddNoteRepositoryInterface
import javax.inject.Inject

class SaveNoteUseCase @Inject constructor(
    private val addNoteRepositoryInterface: AddNoteRepositoryInterface
) {
    suspend operator fun invoke(
        context: Context,
        news: NewsEntity?,
        noteContent: String,
        selectedImage: List<Uri>,
        selectedTags: List<TagEntity>
    ) = addNoteRepositoryInterface.saveNote(
        context = context,
        news = news,
        noteContent = noteContent,
        selectedImage = selectedImage,
        selectedTags = selectedTags
    )
}