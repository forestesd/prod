package com.example.prod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.example.apis.NewsViewModel
import com.example.financedate.FinaceViewModel
import com.example.navigation.AppNavigation
import com.example.tickersapi.TickersViewModel
import javax.inject.Inject


class MainActivity : ComponentActivity() {
    @Inject
    lateinit var newsViewModel: NewsViewModel
    @Inject
    lateinit var tickersViewModel: TickersViewModel
    @Inject
    lateinit var financeViewModel: FinaceViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MyApplication).appComponent.inject(this)

        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            AppNavigation(tickersViewModel, newsViewModel, financeViewModel)
        }
    }
}

