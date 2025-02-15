package com.example.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.apis.NewsViewModel
import com.example.home.NewsFeed.NewsFeedMain
import com.example.home.NewsFeed.ProgressBar
import com.example.home.Tickers.TickersFeedMain
import com.example.tickersapi.TickersViewModel

@Composable
fun MainScreen(
    newsViewModel: NewsViewModel,
    onCardClicked: (String) -> Unit,
    tickersViewModel: TickersViewModel

) {


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

        if (tickersViewModel.isLoading.value){
            ProgressBar()
        }else{
            TickersFeedMain(tickers, tickersViewModel)
        }
        if (newsViewModel.isLoading.value){
            ProgressBar()
        }else{
            NewsFeedMain(news, newsViewModel, onCardClicked)
        }

    }



}
