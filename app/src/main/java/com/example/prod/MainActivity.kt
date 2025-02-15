package com.example.prod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.example.apis.NewsRepository
import com.example.apis.NewsViewModel
import com.example.navigation.AppNavigation
import com.example.tickersapi.TickersRepository
import com.example.tickersapi.TickersViewModel
import javax.inject.Inject


class MainActivity : ComponentActivity() {

    @Inject lateinit var newsRepository: NewsRepository
    @Inject lateinit var tickersRepository: TickersRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            AppNavigation(newsRepository, tickersRepository)
        }
    }
}

