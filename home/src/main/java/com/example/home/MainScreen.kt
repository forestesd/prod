package com.example.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.apis.Article
import com.example.apis.NewsViewModel
import com.example.home.newsFeed.NewsFeedMain
import com.example.home.newsFeed.ProgressBar
import com.example.home.search.SearchScreen
import com.example.home.shimmer.NewsShimmerListItem
import com.example.home.shimmer.TickersShimmer
import com.example.home.tickers.TickersFeedMain
import com.example.home.tickers.TickersProgressBar
import com.example.tickersapi.TickersViewModel

@Composable
fun MainScreen(
    newsViewModel: NewsViewModel,
    onCardClicked: (Article) -> Unit,
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
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(bottom = 10.dp, start = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ){
                items(10){
                    TickersShimmer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }

        } else {
            TickersFeedMain(tickers,tickersViewModel)
        }

        if (newsViewModel.isLoading.value) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp),
            ){
                items(10){
                    NewsShimmerListItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }

        } else {
            NewsFeedMain(
                if (newsViewModel.isSearching.value) serchNews else news,
                newsViewModel,
                onCardClicked
            )
        }

    }


}
