package com.example.apis

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TimesApiService {
    @GET("content/{source}/{section}.json")
    suspend fun getNews(
        @Path("source") source: String,
        @Path("section") section: String,
        @Query("api-key") apiKey: String
    ): TimesResponse
}