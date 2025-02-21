package com.example.home.tickers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.home.R
import com.example.tickersapi.TickerUi
import com.example.tickersapi.TickersViewModel

@Composable
fun TickersFeedMain(tickers: List<TickerUi>, tickersViewModel: TickersViewModel) {

    if (tickersViewModel.isSearching.value && tickers.isNotEmpty()) {
        SearchTickersFeed(tickers)
    } else if (!tickersViewModel.isSearching.value){
        MainTickersFeed(tickers)
    }

}

@Composable
fun MainTickersFeed(tickers: List<TickerUi>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(bottom = 10.dp, start = 10.dp),
        verticalAlignment = Alignment.CenterVertically,

        ) {
        items(tickers, key = { item -> item.symbol }) { item ->
            CardTicker(item, false)
        }


    }
}

@Composable
fun SearchTickersFeed(tickers: List<TickerUi>) {

    val cardHeight = 80.dp
    val visibleCards = 3
    val totalHeight = cardHeight * visibleCards

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(totalHeight)
            .padding(horizontal = 16.dp)
            .padding(bottom = 10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(tickers, key = { item -> item.symbol }) { item ->
            CardTicker(item, true)
        }
    }
}

@Composable
fun CardTicker(item: TickerUi, isSearch: Boolean) {
    Card(
        modifier = Modifier
            .wrapContentWidth()
            .height(80.dp)
            .padding(end = 10.dp, bottom = if (isSearch) 5.dp else 0.dp),

        ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            AsyncImage(
                model = item.logoUrl,
                modifier = Modifier
                    .padding(start = 5.dp)
                    .size(45.dp)
                    .clip(CircleShape),
                placeholder = painterResource(R.drawable.placeholder),
                error = painterResource(R.drawable.placeholder),
                contentScale = ContentScale.Crop,
                contentDescription = "Логотип тикера"
            )
            Text(
                text = item.name,
                modifier = Modifier.padding(end = 12.dp, start = 5.dp)
            )
            if (isSearch) {
                Spacer(modifier = Modifier.weight(1f))
            }
            Text(
                text = if (!isSearch)
                    "${item.price}$ (${item.priceChangePercent})%" else "${item.price}$",

                color = if (!isSearch) item.priceColor else Color.Black,
                modifier = Modifier.padding(end = 10.dp)
            )
            if (!isSearch) {
                if (item.isUp) {
                    Icon(
                        painterResource(R.drawable.ticker_up_icon),
                        contentDescription = "Акции на подъёме",

                        )
                } else {
                    Icon(
                        painterResource(R.drawable.ticker_down_icon),
                        contentDescription = "Акции опускаются в цене",
                    )
                }
            }
        }
    }
}