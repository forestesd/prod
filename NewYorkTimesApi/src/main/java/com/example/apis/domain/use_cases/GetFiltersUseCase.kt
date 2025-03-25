package com.example.apis.domain.use_cases

import com.example.apis.domain.repository.NewsRepositoryInterface
import javax.inject.Inject

class GetFiltersUseCase @Inject constructor(
    private val newsRepositoryInterface: NewsRepositoryInterface
) {
    suspend operator fun invoke(): List<String> = newsRepositoryInterface.getFilters()
}