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
import com.example.apis.data.NewsViewModel
import com.example.financedate.FinanceViewModel
import com.example.financeui.FinanceMainScreen
import com.example.home.MainScreen
import com.example.home.newsFeed.WebViewNews
import com.example.notesdata.AddNoteViewModel
import com.example.notesdata.NotesViewModel
import com.example.notesdata.db.NewsEntity
import com.example.notesui.AddNoteScreen
import com.example.notesui.FavoritesScreen
import com.example.notesui.NotesMainScreen
import com.example.tickersapi.data.TickersViewModel
import com.example.ui.NavigationUI

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
                    navController.navigate("webViewNews?imageUrl=$encodeImgeUrl&newsUrl=$encodeNewsUrl&title=$encodeTitle")
                }, tickersViewModel)
            }
            composable("webViewNews?imageUrl={imageUrl}&newsUrl={newsUrl}&title={title}") {
                val newsUrl = it.arguments?.getString("newsUrl").toString()
                val imageUrl = it.arguments?.getString("imageUrl").toString()
                val title = it.arguments?.getString("title").toString()
                WebViewNews(
                    newsUrl,
                    newsViewModel,
                    tickersViewModel,
                    onBack = { navController.navigate("main") },
                    onShare = {
                        navController.navigate("addNote?imageUrl=$imageUrl&newsUrl=$newsUrl&title=$title")
                    }
                )
            }
            composable("finance") {
                FinanceMainScreen(financeViewModel)
            }
            composable("notes") {
                NotesMainScreen(
                    notesViewModel,
                    onNewsClicked = { navController.navigate("webViewNews?imageUrl=${it.imageUrl}&newsUrl=${it.newsUrl}&title=${it.title}") },
                    onAddButtonClicked = { navController.navigate("addNote") },
                    onFavoritesClicked = { navController.navigate("favorites") },
                )
            }
            composable("favorites") {
                FavoritesScreen(
                    notesViewModel,
                    onBack = { navController.navigate("notes") },
                    onNewsClicked = { navController.navigate("webViewNews?imageUrl=${it.imageUrl}&newsUrl=${it.newsUrl}&title=${it.title}") },
                    )
            }
            composable("addNote?imageUrl={imageUrl}&newsUrl={newsUrl}&title={title}") {
                val newsUrl = it.arguments?.getString("newsUrl").toString()
                val imageUrl = it.arguments?.getString("imageUrl").toString()
                val title = it.arguments?.getString("title").toString()

                AddNoteScreen(
                    onBack = { navController.navigate("notes") },
                    onPickImage = onPickImageClick,
                    addNoteViewModel = addNoteViewModel,
                    notesViewModel = notesViewModel,
                    news = NewsEntity(
                        imageUrl = imageUrl,
                        articleUrl = newsUrl,
                        title = title
                    )
                )
            }
        }
    }

}