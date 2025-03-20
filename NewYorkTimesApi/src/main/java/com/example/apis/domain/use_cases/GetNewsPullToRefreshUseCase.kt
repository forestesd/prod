package com.example.apis.domain.use_cases

import com.example.apis.domain.repository.NewsRepository
import javax.inject.Inject

class GetNewsPullToRefreshUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(source: String, section: String, apiKey: String) =
        newsRepository.getNewsPullToRefresh(source, section, apiKey)
}