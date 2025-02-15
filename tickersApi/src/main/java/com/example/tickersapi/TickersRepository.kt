package com.example.tickersapi

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import javax.inject.Inject

class TickersRepository @Inject constructor(
    private val api: TickersApiService
) {
    private val tickers =
        listOf("AAPL", "MSFT ", "GOOGL", "AMZN", "TSLA", "META", "NVDA", "NFLX", "AMD", "INTC")


    suspend fun getCompanyInfo(apiKey: String): List<TickerUi> {
        val tickersUi = mutableListOf<TickerUi>()
        for (symbol in tickers) {
            val companyProfile = api.getCompanyInfo("", "")
            val stockQuote = api.getInfoTicker("asd", "")
           tickersUi.add(TickersUiMapper(companyProfile, stockQuote))
        }
        return tickersUi
    }
}