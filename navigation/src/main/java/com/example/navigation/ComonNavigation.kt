package com.example.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.FinanceScreen
import com.example.ui.BottomNavigationBar
import com.example.ui.Newsfeed

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    BottomNavigationBar(onNavigate = { navController.navigate(it) })
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {

        }
        composable("finance") {
            FinanceScreen()
        }
        composable("newsFeed") {
            Newsfeed()
        }
    }

}