package com.example.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.apis.NewsViewModel
import com.example.home.newsFeed.NewsFeedMain
import com.example.home.newsFeed.ProgressBar
import com.example.home.tickers.TickersFeedMain
import com.example.home.tickers.TickersProgressBar
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
        Spacer(modifier = Modifier.fillMaxWidth().height(100.dp))

        if (tickersViewModel.isLoading.value){
            TickersProgressBar()
        }else {
            TickersFeedMain(tickers)
        }

        if (newsViewModel.isLoading.value){
            ProgressBar()
        }else{
            NewsFeedMain(news, newsViewModel, onCardClicked)
        }

    }



}
