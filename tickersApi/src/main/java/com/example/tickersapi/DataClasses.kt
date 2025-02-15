package com.example.tickersapi

data class StockQuote(
    val c: Float, // Текущая цена
    val h: Float, // Максимальная цена за день
    val l: Float, // Минимальная цена за день
    val o: Float, // Цена открытия
    val pc: Float // Цена закрытия предыдущего дня
)