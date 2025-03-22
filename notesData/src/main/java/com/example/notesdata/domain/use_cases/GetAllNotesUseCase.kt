package com.example.notesdata.domain.use_cases

import com.example.notesdata.data.repository.NotesRepository
import com.example.notesdata.domain.models.PostUi
import com.example.notesdata.domain.repository.NoteRepositoryInterface
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllNotesUseCase @Inject constructor(
    private val notesRepositoryInterface: NoteRepositoryInterface
) {
    suspend operator fun invoke(): Flow<List<PostUi>> = notesRepositoryInterface.getAllNotes()
}