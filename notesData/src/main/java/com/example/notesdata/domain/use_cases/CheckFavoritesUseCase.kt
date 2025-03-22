package com.example.notesdata.domain.use_cases

import com.example.notesdata.domain.repository.NoteRepositoryInterface
import javax.inject.Inject

class CheckFavoritesUseCase @Inject constructor(
    private val noteRepositoryInterface: NoteRepositoryInterface
) {
    suspend operator fun invoke(id: Long) = noteRepositoryInterface.checkFavorite(id)
}