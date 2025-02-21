package com.example.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.apis.Article
import com.example.apis.NewsViewModel
import com.example.home.newsFeed.NewsFeedMain
import com.example.home.search.SearchScreen
import com.example.home.shimmer.NewsShimmerListItem
import com.example.home.shimmer.TickersShimmer
import com.example.home.tickers.TickersFeedMain
import com.example.tickersapi.TickersViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    newsViewModel: NewsViewModel,
    onCardClicked: (Article) -> Unit,
    tickersViewModel: TickersViewModel

) {
    var isRefreshing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(Unit) {
        newsViewModel.loadNews()
        tickersViewModel.loadTickers()
    }
    val onRefresh: () -> Unit = {
        coroutineScope.launch {
            isRefreshing = true
            newsViewModel.loadNewsPullToRefresh()
            tickersViewModel.loadTickers()

        }

    }
    LaunchedEffect(newsViewModel.news.value, tickersViewModel.tickers.value) {
        isRefreshing = false
    }


    val news by newsViewModel.news
    val serchNews by newsViewModel.searchNews
    val tickers by tickersViewModel.tickers



    PullToRefreshBox(
        modifier = Modifier.fillMaxSize(),
        state = pullToRefreshState,
        onRefresh = onRefresh,
        isRefreshing = isRefreshing
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

            SearchScreen(newsViewModel, tickersViewModel)

            HorizontalDivider(modifier = Modifier.padding(10.dp))


            if (tickersViewModel.isLoading.value) {
                if (!tickersViewModel.isSearching.value) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .padding(bottom = 10.dp, start = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        items(10) {
                            TickersShimmer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                tickersViewModel.isSearching.value
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.35f)
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 10.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(10) {
                            TickersShimmer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                tickersViewModel.isSearching.value
                            )
                        }
                    }
                }

            } else {
                TickersFeedMain(tickers, tickersViewModel)
            }

            if (newsViewModel.isLoading.value) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(16.dp),
                ) {
                    items(10) {
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



}
