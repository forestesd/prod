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
import com.example.apis.NewsViewModel
import com.example.financedate.FinanceViewModel
import com.example.financeui.FinanceMainScreen
import com.example.home.MainScreen
import com.example.home.newsFeed.WebViewNews
import com.example.notesdata.AddNoteViewModel
import com.example.notesdata.NotesViewModel
import com.example.notesui.AddNoteScreen
import com.example.notesui.NotesMainScreen
import com.example.tickersapi.TickersViewModel
import com.example.ui.NavigationUI
import com.example.ui.Newsfeed

@Composable
fun AppNavigation(
    tickersViewModel: TickersViewModel,
    newsViewModel: NewsViewModel,
    financeViewModel: FinanceViewModel,
    notesViewModel: NotesViewModel,
    addNoteViewModel: AddNoteViewModel,
    onPickImageClick: () -> Unit
) {


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
            navController.navigate("notes")
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = "main",
            modifier = Modifier.padding(paddingValues)

        ) {
            composable("main") {
                MainScreen(newsViewModel, {
                    val encodeImgeUrl = newsViewModel.getImageUrlForArticle(it)
                    val encodeNewsUrl = Uri.encode(it.url)
                    val encodeTitle = it.title
                    navController.navigate("webViewNews/$encodeImgeUrl/$encodeNewsUrl/$encodeTitle")
                }, tickersViewModel)
            }
            composable("webViewNews/{imageUrl}/{newsUrl}/{title}") {
                val newsUrl = it.arguments?.getString("newsUrl").toString()
                val imageUrl = it.arguments?.getString("imageUrl").toString()
                val title = it.arguments?.getString("title").toString()
                WebViewNews(
                    newsUrl,
                    newsViewModel,
                    tickersViewModel,
                    onBack = {navController.navigate("main")}
                )
            }
            composable("finance") {
                FinanceMainScreen(financeViewModel)
            }
            composable("notes") {
                NotesMainScreen(notesViewModel, addNoteViewModel, onPickImageClick, {navController.navigate("addNote")})
            }
            composable("addNote") {
                AddNoteScreen(
                    onBack = { navController.navigate("notes") },
                    onPickImage = onPickImageClick,
                    addNoteViewModel = addNoteViewModel,
                    notesViewModel = notesViewModel
                )
            }
        }
    }

}