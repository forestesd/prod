package com.example.notesdata.domain.use_cases

import com.example.notesdata.domain.repository.NoteRepositoryInterface
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val noteRepositoryInterface: NoteRepositoryInterface
) {
    suspend operator fun invoke(id: Long, isFavorite: Boolean) =
        noteRepositoryInterface.toggleFavorite(id, isFavorite)
}