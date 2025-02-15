package com.example.tickersapi

import retrofit2.http.GET
import retrofit2.http.Query

interface TickersApiService {
    @GET("quote")
    suspend fun getInfoTicker(
        @Query("symbol") symbol: String,
        @Query("token") token: String
    )
}