package com.example.tickersapi.data.remote

import com.example.tickersapi.domain.models.CompanyProfileResponse
import com.example.tickersapi.domain.models.SearchCompany
import com.example.tickersapi.domain.models.StockQuote
import retrofit2.http.GET
import retrofit2.http.Query

interface TickersApiService {
    @GET("quote")
    suspend fun getInfoTicker(
        @Query("symbol") symbol: String,
        @Query("token") token: String
    ): StockQuote

    @GET("stock/profile2")
    suspend fun getCompanyInfo(
        @Query("symbol") symbol: String,
        @Query("token") token: String
    ): CompanyProfileResponse
    @GET("search")
    suspend fun searchCompany(
        @Query("q") q: String,
        @Query("exchange") exchange: String,
        @Query("token") token: String
    ): SearchCompany
}