package com.example.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.apis.NewsViewModel
import com.example.home.newsFeed.NewsFeedMain
import com.example.home.newsFeed.ProgressBar
import com.example.home.search.SearchScreen
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
    val serchNews by newsViewModel.searchNews
    val tickers by tickersViewModel.tickers
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        SearchScreen(newsViewModel,tickersViewModel)

        HorizontalDivider(modifier = Modifier.padding(10.dp))




        if (tickersViewModel.isLoading.value) {
            TickersProgressBar()
        } else {
            TickersFeedMain(tickers,tickersViewModel)
        }

        if (newsViewModel.isLoading.value) {
            ProgressBar()
        } else {
            NewsFeedMain(
                if (newsViewModel.isSearching.value) serchNews else news,
                newsViewModel,
                onCardClicked
            )
        }

    }


}
