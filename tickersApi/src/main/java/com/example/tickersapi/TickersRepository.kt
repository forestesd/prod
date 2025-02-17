package com.example.tickersapi


import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.InputStreamReader

import javax.inject.Inject

class TickersRepository @Inject constructor(
    private val context: Context,
    private val api: TickersApiService
) {
    private val tickers = loadTickersFromJSON()

    private fun loadTickersFromJSON(): List<String> {
        val jsonFile = context.resources.openRawResource(R.raw.tickers)
        val reader = InputStreamReader(jsonFile)
        val jsonStr = reader.readText()

        val jsonArray = JSONArray(jsonStr)
        val tickersList = mutableListOf<String>()
        for (i in 0..<jsonArray.length()) {
            tickersList.add(jsonArray.getString(i))
        }
        return tickersList
    }

    suspend fun getCompanyInfo(apiKey: String): List<TickerUi> {
        val tickersUi = mutableListOf<TickerUi>()

        coroutineScope {
            val jobs = tickers.map { symbol ->
                async(Dispatchers.IO) {
                    val companyProfile = getCompanyProfile(symbol,apiKey)
                    val stockQuote = getCompanyTicker(symbol, apiKey)
                    tickersUi.add(tickersUiMapper(companyProfile, stockQuote))
                }
            }
            jobs.awaitAll()
        }

        return tickersUi
    }

    suspend fun searchCompany(apiKey: String, q: String, exchange: String): List<TickerUi> {
        val tickerUi = mutableListOf<TickerUi>()
        coroutineScope {
            val companiesSearch = api.searchCompany(q, exchange, apiKey)
            val jobs = companiesSearch.result.map { item ->
                async(Dispatchers.IO) {
                    val companyProfile = getCompanyProfile(item.symbol,apiKey)
                    val companyTicker = getCompanyTicker(item.symbol, apiKey)
                    tickerUi.add(tickersUiMapper(companyProfile,companyTicker))
                }
            }
            jobs.awaitAll()
        }
        return tickerUi

    }

    private suspend fun getCompanyProfile(symbol: String, apiKey: String): CompanyProfileResponse{
        var companyProfile: CompanyProfileResponse
        withContext(Dispatchers.IO) {
            companyProfile = api.getCompanyInfo(symbol, apiKey)
        }
        return companyProfile
    }

    private suspend fun getCompanyTicker(symbol: String,apiKey: String): StockQuote{
        var ticker: StockQuote
        withContext(Dispatchers.IO){
            ticker = api.getInfoTicker(symbol, apiKey)
        }
        return ticker
    }
}