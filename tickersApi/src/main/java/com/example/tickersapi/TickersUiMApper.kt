package com.example.tickersapi

import android.graphics.Color


fun TickersUiMapper(companyProfileResponse: CompanyProfileResponse,stockQuote: StockQuote):TickerUi{

    val priceChanges = stockQuote.c - stockQuote.pc
    val priceChangePercent = (priceChanges/stockQuote.c)*100

    val isUp = priceChanges>0
    val priceColor = if (isUp) Color.GREEN else Color.RED

    return TickerUi(
        name = companyProfileResponse.name.toString(),
        logoUrl = companyProfileResponse.logo.toString(),
        price = stockQuote.c,
        priceChangePercent = priceChangePercent,
        isUp =  isUp,
        priceColor = priceColor
    )

}