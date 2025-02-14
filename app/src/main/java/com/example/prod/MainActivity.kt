package com.example.prod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.example.navigation.AppNavigation


class MainActivity : ComponentActivity() {

    private lateinit var appComponent: AppComponent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        appComponent = DaggerAppComponent.builder()
            .timesApiModule(TimesApiModule())
            .build()
        val newsRepository = appComponent.getNewsRepository()
        setContent {
            AppNavigation(newsRepository)
        }
    }
}

