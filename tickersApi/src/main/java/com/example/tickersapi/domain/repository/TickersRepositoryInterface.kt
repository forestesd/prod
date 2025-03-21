package com.example.tickersapi.domain.repository

import com.example.tickersapi.domain.models.TickerUi

interface TickersRepositoryInterface {
    suspend fun getCompanyInfo(apiKey: String): List<TickerUi>

    suspend fun searchCompany(apiKey: String, q: String, exchange: String): List<TickerUi>

    suspend fun webSocketOpen()
}