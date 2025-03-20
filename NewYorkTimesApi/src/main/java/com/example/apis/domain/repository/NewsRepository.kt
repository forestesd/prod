package com.example.apis.domain.repository

import com.example.apis.domain.models.Article
import com.example.apis.domain.models.Docs

interface NewsRepository {
    suspend fun getNews(source: String, section: String, apiKey: String): List<Article>

    suspend fun getNewsPullToRefresh(source: String, section: String, apiKey: String): List<Article>

    suspend fun getSearchNews(q: String, apiKey: String): List<Docs>
}