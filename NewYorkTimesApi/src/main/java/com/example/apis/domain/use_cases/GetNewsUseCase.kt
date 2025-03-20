package com.example.apis.domain.use_cases

import com.example.apis.domain.models.Article
import com.example.apis.domain.repository.NewsRepository
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(source: String, section: String, apiKey: String) =
        newsRepository.getNews(source, section, apiKey)
}