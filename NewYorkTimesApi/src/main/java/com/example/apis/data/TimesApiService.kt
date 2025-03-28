package com.example.apis.data

import com.example.apis.domain.models.SearchTimesResponse
import com.example.apis.domain.models.TimesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TimesApiService {
     @GET("articlesearch.json")
    suspend fun searchNews(
        @Query ("q") q: String?="",
        @Query("fq") filterQuery: String? = null,
        @Query("page") page: Int? = 0,
        @Query ("api-key") apiKey: String
    ): SearchTimesResponse
}