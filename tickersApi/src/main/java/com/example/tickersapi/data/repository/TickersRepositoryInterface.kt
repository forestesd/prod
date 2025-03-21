package com.example.tickersapi.data.repository


import android.content.Context
import com.example.tickersapi.R
import com.example.tickersapi.domain.models.TickerUi
import com.example.tickersapi.data.remote.TickersApiService
import com.example.tickersapi.data.remote.TickersWebSocket
import com.example.tickersapi.data.utils.tickersUiMapper
import com.example.tickersapi.domain.repository.TickersRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import okhttp3.OkHttpClient
import org.json.JSONArray
import retrofit2.HttpException
import java.io.InputStreamReader
import javax.inject.Inject

class TickersRepositoryInterface @Inject constructor(
    private val context: Context,
    private val api: TickersApiService,
    client: OkHttpClient,
    apiUrl: String,
) : TickersRepositoryInterface {
    private val tickers = loadTickersFromJSON()
    private val webSocket = TickersWebSocket(client, apiUrl)


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

    override suspend fun getCompanyInfo(apiKey: String): List<TickerUi> {
        val tickersUi = mutableListOf<TickerUi>()

        coroutineScope {
            val jobs = tickers.map { symbol ->
                async(Dispatchers.IO) {
                    while (true) {
                        try {
                            val companyProfile = api.getCompanyInfo(symbol, apiKey)
                            val stockQuote = api.getInfoTicker(symbol, apiKey)
                            tickersUi.add(tickersUiMapper(companyProfile, stockQuote))

                            break
                        } catch (_: HttpException) {

                        }
                    }
                }
            }
            jobs.awaitAll()
        }

        return tickersUi
    }

    override suspend fun searchCompany(
        apiKey: String,
        q: String,
        exchange: String
    ): List<TickerUi> {
        val tickerUi = mutableListOf<TickerUi>()

        coroutineScope {
            val companiesSearch = api.searchCompany(q, exchange, apiKey)
            val jobs = companiesSearch.result.distinctBy { it.symbol }.map { item ->
                async(Dispatchers.IO) {
                    while (true) {
                        try {
                            val companyProfile = api.getCompanyInfo(item.symbol, apiKey)
                            val companyTicker = api.getInfoTicker(item.symbol, apiKey)
                            val mappedTicker = tickersUiMapper(companyProfile, companyTicker)
                            synchronized(tickerUi) {
                                if (!tickerUi.contains(mappedTicker)) {
                                    tickerUi.add(mappedTicker)
                                }
                            }
                            break
                        } catch (_: HttpException) {

                        }
                    }
                }
            }
            jobs.awaitAll()
        }
        return tickerUi.filter { it.name != "null" && it.price.toDouble() != 0.0 }
            .distinctBy { it.symbol }

    }

    override suspend fun webSocketOpen() {
        if (!webSocket.isConnected) {
            webSocket.connect()
        }
        coroutineScope {
            val jobs = tickers.map { symbol ->
                async(Dispatchers.IO) {
                    while (true) {
                        try {
                            webSocket.subscribeToTicker(symbol)
                            break
                        } catch (_: HttpException) {

                        }
                    }
                }

            }
            jobs.awaitAll()
        }
    }
}