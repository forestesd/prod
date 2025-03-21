package com.example.tickersapi.domain.models

import androidx.compose.ui.graphics.Color


data class StockQuote(
    val c: Double,
    val h : Double,
    val dp: Double,
    val l: Double,
    val o: Double,
    val pc: Double,
    val d: Double
)

data class TickersWebSocketData(
    val p: Double,
    val s: String,
    val v: Int,
    val t: Long
)
data class CompanyProfileResponse(
    val name: String?,
    val logo: String?,
    val weburl: String?,
    val ticker: String?
)

data class TickerUi(
    val name: String,
    val symbol: String,
    val logoUrl: String,
    val price: String,
    val priceChangePercent: String,
    val isUp: Boolean,
    val priceColor: Color,
)

data class SearchCompany(
    val result: List<SearchCompanyProfile>
)

data class SearchCompanyProfile(
    val symbol: String
)