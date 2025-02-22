package com.example.tickersapi

import androidx.compose.ui.graphics.Color
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.round


fun tickersUiMapper(companyProfileResponse: CompanyProfileResponse, stockQuote: StockQuote):TickerUi{
    val symbols = DecimalFormatSymbols(Locale.US).apply {
        decimalSeparator = '.'
    }

    val isUp = stockQuote.d>0
    val priceColor = if (isUp) Color(0xFF1BBE31) else Color.Red

    return TickerUi(
        name = companyProfileResponse.name.toString(),
        symbol =  companyProfileResponse.ticker.toString(),
        logoUrl = companyProfileResponse.logo.toString(),
        price = DecimalFormat("#.########", symbols).format(stockQuote.c),
        priceChangePercent = (round(stockQuote.dp*100)/100).toString(),
        isUp =  isUp,
        priceColor = priceColor
    )

}