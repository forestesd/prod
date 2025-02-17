package com.example.tickersapi

import androidx.compose.ui.graphics.Color


data class StockQuote(
    val c: Double, // Текущая цена
    val h: Double, // Максимальная цена за день
    val l: Double, // Минимальная цена за день
    val o: Double, // Цена открытия
    val pc: Double // Цена закрытия предыдущего дня
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