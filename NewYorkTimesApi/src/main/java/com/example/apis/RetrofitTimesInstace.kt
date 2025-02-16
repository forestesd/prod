package com.example.apis

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitTimesInstance {
    private const val BASE_URL = "https://api.nytimes.com/svc/news/v3/"

    val api: TimesApiService by lazy {

        val logginigInspector = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logginigInspector)
            .build()


        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TimesApiService::class.java)
    }
}
object RetrofitSearchTimesInstance {
    private const val SEARCH_BASE_URL = "https://api.nytimes.com/svc/search/v2/"

    val api: TimesApiService by lazy {

        val loggingInspector = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInspector)
            .build()

        Retrofit.Builder()
            .baseUrl(SEARCH_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TimesApiService::class.java)
    }
}