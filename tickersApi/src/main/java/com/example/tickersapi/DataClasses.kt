package com.example.tickersapi

import androidx.compose.ui.graphics.Color


data class StockQuote(
    val c: Float, // Текущая цена
    val h: Float, // Максимальная цена за день
    val l: Float, // Минимальная цена за день
    val o: Float, // Цена открытия
    val pc: Float // Цена закрытия предыдущего дня
)
data class CompanyProfileResponse(
    val name: String?,
    val logo: String?,
    val weburl: String?,
    val ticker: String?
)

data class TickerUi(
    val name: String,
    val logoUrl: String,
    val price: Float,
    val priceChangePercent: Float,
    val isUp: Boolean,
    val priceColor: Color,
)