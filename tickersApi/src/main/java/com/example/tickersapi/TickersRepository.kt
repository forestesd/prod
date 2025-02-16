package com.example.tickersapi


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

import javax.inject.Inject

class TickersRepository @Inject constructor(
    private val api: TickersApiService
) {
    private val tickers =
        listOf("AAPL", "MSFT", "GOOGL", "AMZN", "TSLA", "META", "NVDA", "NFLX", "AMD", "INTC")


    suspend fun getCompanyInfo(apiKey: String): List<TickerUi> {
        val tickersUi = mutableListOf<TickerUi>()

        coroutineScope {
            val jobs = tickers.map { symbol ->
                async(Dispatchers.IO) {
                    val companyProfile = api.getCompanyInfo(symbol, apiKey)
                    val stockQuote = api.getInfoTicker(symbol, apiKey)
                    tickersUi.add(tickersUiMapper(companyProfile, stockQuote))
                }
            }
            jobs.awaitAll()
        }






        return tickersUi
    }
}