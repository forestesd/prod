package com.example.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.apis.domain.models.Article
import com.example.apis.data.NewsViewModel
import com.example.home.newsFeed.NewsCard
import com.example.home.search.SearchScreen
import com.example.home.shimmer.NewsShimmerListItem
import com.example.home.shimmer.TickersShimmer
import com.example.home.tickers.TickersFeedMain
import com.example.tickersapi.data.TickersViewModel
import kotlinx.coroutines.flow.collectLatest
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
            newsViewModel.loadNewsPullToRefresh()
            tickersViewModel.loadTickers()
        }

    }
    val tickersLoading by tickersViewModel.isLoading.collectAsState()
    val newsLoading by newsViewModel.isLoading.collectAsState()
    val isSearchingTickers by tickersViewModel.isSearching.collectAsState()
    val isSearchingNews by newsViewModel.isSearching.collectAsState()


    val isLoading = newsLoading || tickersLoading

    LaunchedEffect(isLoading) {
        isRefreshing = isLoading
    }

    val isMoreLoading by newsViewModel.isMoreLoading.collectAsState()

    val listState = rememberLazyListState()
    val loadMore = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItemsNumber = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            totalItemsNumber > 0 && lastVisibleItemIndex >= (totalItemsNumber - 3)
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { loadMore.value }
            .collectLatest { shouldLoadMore ->
                if (shouldLoadMore) {
                    newsViewModel.onScroll()
                }
            }
    }

    val news by newsViewModel.news.collectAsState()
    val serchNews by newsViewModel.searchNews.collectAsState()
    val tickers by tickersViewModel.tickers.collectAsState()



    PullToRefreshBox(
        modifier = Modifier.fillMaxSize(),
        state = pullToRefreshState,
        onRefresh = onRefresh,
        isRefreshing = isRefreshing
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            state = listState
        ) {
            item {
                SearchScreen(newsViewModel, tickersViewModel)
                HorizontalDivider(modifier = Modifier.padding(10.dp))
            }

            item {
                if (tickersLoading) {
                    if (!isSearchingTickers) {
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
                                    isSearchingTickers
                                )
                            }
                        }
                    } else {
                        val cardHeight = 82.dp
                        val visibleCards = 3
                        val totalHeight = cardHeight * visibleCards
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(totalHeight)
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 10.dp),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            repeat(10) {
                                TickersShimmer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    isSearchingTickers
                                )
                            }
                        }
                    }

                } else {
                    TickersFeedMain(tickers, tickersViewModel)
                }
            }
            if (!isSearchingNews) {
                item {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(news.filters, key = { item -> item }) { item ->
                            FilterChip(
                                selected = item == news.selectedSection,
                                onClick = { newsViewModel.changeSelectedFilter(item) },
                                label = {
                                    Text(
                                        text = item
                                    )
                                }
                            )
                        }
                    }

                }
            }

            if (newsLoading) {

                items(10) {
                    NewsShimmerListItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }

            } else {
                items(
                    if (isSearchingNews) serchNews else news.news,
                    key = { item -> item.title }) { item ->
                    NewsCard(
                        item, newsViewModel
                    ) {
                        onCardClicked(
                            item
                        )
                    }
                }
                if (isMoreLoading){
                    items(3) {
                        NewsShimmerListItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }
            }


        }
    }


}
