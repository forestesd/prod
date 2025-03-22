package com.example.notesdata.domain.use_cases

import com.example.notesdata.domain.repository.NoteRepositoryInterface
import javax.inject.Inject

class GetAllTagsUseCase @Inject constructor(
    private val noteRepositoryInterface: NoteRepositoryInterface
) {
    suspend operator fun invoke() = noteRepositoryInterface.getAllTags()
}