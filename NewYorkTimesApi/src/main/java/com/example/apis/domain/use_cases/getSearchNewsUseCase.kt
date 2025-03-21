package com.example.apis.domain.use_cases

import com.example.apis.domain.repository.NewsRepositoryInterface
import javax.inject.Inject

class GetSearchNewsUseCase @Inject constructor(
    private val newsRepositoryInterface: NewsRepositoryInterface
) {
    suspend operator fun invoke(q: String, apiKey: String) = newsRepositoryInterface.getSearchNews(q, apiKey)
}