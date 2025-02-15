package com.example.navigation

import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.apis.NewsRepository
import com.example.home.MainScreen
import com.example.home.WebViewNews
import com.example.ui.FinanceScreen
import com.example.ui.NavigationUI
import com.example.ui.Newsfeed

@Composable
fun AppNavigation(newsRepository: NewsRepository) {

    val navController = rememberNavController()
    var selectedItem by remember { mutableIntStateOf(0) }

    NavigationUI(
        selectedItem = selectedItem,
        onMainClick = {
            selectedItem = 0
            navController.navigate("main")
        },
        onFinanceClick = {
            selectedItem = 1
            navController.navigate("finance")
        },
        onNewsFeedClick = {
            selectedItem = 2
            navController.navigate("newsFeed")
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = "main",
            modifier = Modifier.padding(paddingValues)

        ) {
            composable("main") {
                MainScreen(newsRepository, {
                    val encodeUrl = Uri.encode(it)
                    navController.navigate("webViewNews/$encodeUrl")
                })
            }
            composable("webViewNews/{url}") {
                val decodeUrl = it.arguments?.getString("url").toString()
                WebViewNews(decodeUrl,{navController.navigate("main")})
            }
            composable("finance") {
                FinanceScreen()
            }
            composable("newsFeed") {
                Newsfeed()
            }
        }
    }

}