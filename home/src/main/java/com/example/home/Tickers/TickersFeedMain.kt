package com.example.home.Tickers

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.home.R
import com.example.tickersapi.TickerUi
import com.example.tickersapi.TickersViewModel

@Composable
fun TickersFeedMain(tickers: List<TickerUi>, viewModel: TickersViewModel) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        items(tickers, key = { item -> item.name }) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AsyncImage(
                        model = item.logoUrl,
                        contentDescription = "Логотип тикера"
                    )
                    Text(
                        text = item.name
                    )
                    Text(
                        text = item.price.toString(),
                        color = Color(item.priceColor)
                    )
                    Text(
                        text = "(${item.priceChangePercent}%)",
                        color = Color(item.priceColor)
                    )
                    if (item.isUp) {
                        Icon(
                            painterResource(R.drawable.ticker_up_icon),
                            contentDescription = "Акции на подъёме",

                        )
                    }else{
                        Icon(
                            painterResource(R.drawable.ticker_down_icon),
                            contentDescription = "Акции опускаются в цене",

                            )
                    }
                }
            }
        }


    }
}