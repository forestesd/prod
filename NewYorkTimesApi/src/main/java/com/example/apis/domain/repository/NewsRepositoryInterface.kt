package com.example.apis.domain.repository

import com.example.apis.domain.models.Article
import com.example.apis.domain.models.Docs

interface NewsRepositoryInterface {
    suspend fun getNews(source: String, apiKey: String, section: String, page: Int ): List<Docs>

    suspend fun getNewsPullToRefresh(source: String, section: String, apiKey: String): List<Docs>

    suspend fun getSearchNews(q: String, apiKey: String): List<Docs>

    suspend fun getFilters():List<String>
}