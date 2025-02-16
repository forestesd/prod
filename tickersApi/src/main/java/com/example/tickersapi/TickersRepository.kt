package com.example.tickersapi


import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.json.JSONArray
import java.io.InputStreamReader

import javax.inject.Inject

class TickersRepository @Inject constructor(
    private val context: Context,
    private val api: TickersApiService
) {
    private val tickers = loadTickersFromJSON()

    private fun loadTickersFromJSON(): List<String>{
        val jsonFile = context.resources.openRawResource(R.raw.tickers)
        val reader = InputStreamReader(jsonFile)
        val jsonStr = reader.readText()

        val jsonArray = JSONArray(jsonStr)
        val tickersList = mutableListOf<String>()
        for (i in 0 ..< jsonArray.length()){
            tickersList.add(jsonArray.getString(i))
        }
        return tickersList
    }

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