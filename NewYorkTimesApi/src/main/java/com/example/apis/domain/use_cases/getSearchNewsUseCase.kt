package com.example.apis.domain.use_cases

import com.example.apis.domain.repository.NewsRepository
import javax.inject.Inject

class GetSearchNewsUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(q: String, apiKey: String) = newsRepository.getSearchNews(q, apiKey)
}