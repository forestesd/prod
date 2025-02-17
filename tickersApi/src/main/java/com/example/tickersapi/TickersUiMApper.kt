package com.example.tickersapi

import androidx.compose.ui.graphics.Color
import kotlin.math.round


fun tickersUiMapper(companyProfileResponse: CompanyProfileResponse, stockQuote: StockQuote):TickerUi{

    val priceChanges = stockQuote.c - stockQuote.pc
    val priceChangePercent = (priceChanges/stockQuote.c)*100

    val isUp = priceChanges>0
    val priceColor = if (isUp) Color(0xFF1BBE31) else Color.Red

    return TickerUi(
        name = companyProfileResponse.name.toString(),
        symbol =  companyProfileResponse.ticker.toString(),
        logoUrl = companyProfileResponse.logo.toString(),
        price = round(stockQuote.c*100)/100,
        priceChangePercent = round(priceChangePercent*100)/100,
        isUp =  isUp,
        priceColor = priceColor
    )

}