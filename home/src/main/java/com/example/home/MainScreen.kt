package com.example.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.apis.NewsRepository
import com.example.apis.NewsViewModel
import com.example.apis.NewsViewModelFactory
import com.example.home.NewsFeed.NewsFeedMain
import com.example.home.NewsFeed.ProgressBar
import com.example.tickersapi.TickersRepository
import com.example.tickersapi.TickersViewModel
import com.example.tickersapi.TickersViewModelFactory

@Composable
fun MainScreen(
    newsRepository: NewsRepository,
    onCardClicked: (String) -> Unit,
    tickersRepository: TickersRepository

) {
    val newsViewModelFactory = NewsViewModelFactory(newsRepository)
    val tickersViewModelFactory = TickersViewModelFactory(tickersRepository)
    val newsViewModel: NewsViewModel = viewModel(factory = newsViewModelFactory)
    val tickersViewModel: TickersViewModel = viewModel(factory = tickersViewModelFactory)


    LaunchedEffect(Unit) {
        newsViewModel.loadNews()
        tickersViewModel.loadTickers()
    }

    val news by newsViewModel.news
    val tickers by tickersViewModel.tickers
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(
            modifier = Modifier.fillMaxWidth().height(150.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth().height(150.dp)
            ) {

            }
        }
        if (newsViewModel.isLoading.value){
            ProgressBar()
        }else{
            NewsFeedMain(news, newsViewModel, onCardClicked)
        }

    }



}
