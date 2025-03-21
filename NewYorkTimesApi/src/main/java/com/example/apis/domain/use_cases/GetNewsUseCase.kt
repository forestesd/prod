package com.example.apis.domain.use_cases

import com.example.apis.domain.repository.NewsRepositoryInterface
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val newsRepositoryInterface: NewsRepositoryInterface
) {
    suspend operator fun invoke(source: String, section: String, apiKey: String) =
        newsRepositoryInterface.getNews(source, section, apiKey)
}