package com.example.home.newsFeed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.apis.Article
import com.example.apis.Docs
import com.example.apis.NewsViewModel
import com.example.home.PxToDp
import com.example.home.R

@Composable
fun <T> NewsFeedMain(news: List<T>, viewModel: NewsViewModel, onCardClicked: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp),

        ) {
        items(news, key = { item ->
            when (item) {
                is Article -> item.title
                is Docs -> item.web_url
                else -> "Unknown"
            }
        }) { item ->
            NewsCrad(
                when (item) {
                    is Article -> item
                    is Docs -> item
                    else -> "Unknown"
                }, viewModel
            ) {
                onCardClicked(
                    when (item) {
                        is Article -> item.url
                        is Docs -> item.web_url
                        else -> "Unknown"
                    }.toString()

                )
            }

        }
    }
}

@Composable
fun <T> NewsCrad(item: T, viewModel: NewsViewModel, onCardClicked: (String) -> Unit) {
    val imageUrl = when (item) {
        is Article -> viewModel.getImageUrlForArticle(item)
        is Docs -> viewModel.getImageUrlForDocs(item)
        else -> null
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(12.dp),
        onClick = {

            when (item) {
                is Article -> item.url
                is Docs -> item.web_url
                else -> "Unknown"
            }.let {
                onCardClicked(
                    it
                )

            }

        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {

            NewsMain(item, imageUrl)
            PublishedSource(item)
        }

    }
}

@Composable
fun <T> NewsMain(newsItem: T, imageUrl: String?) {

    AsyncImage(
        model = imageUrl,
        modifier = Modifier
            .fillMaxWidth()
            .height(PxToDp(400f))
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Gray),
        contentScale = ContentScale.Crop,
        placeholder = painterResource(R.drawable.placeholder),
        contentDescription = "News Image"
    )

    when (newsItem) {
        is Article -> newsItem.title
        is Docs -> newsItem.headline?.main
        else -> "Unknown"
    }?.let {
        Text(
        text = it,
        modifier = Modifier.padding(top = 10.dp),
        fontSize = 16.sp,

        )
    }

    when (newsItem) {
        is Article -> newsItem.abstract
        is Docs -> newsItem.abstract
        else-> "Unknown"
    }.let {
        Text(
            text = it,
            modifier = Modifier.padding(top = 12.dp),
            fontSize = 12.sp
        )
    }
}


@Composable
fun <T> PublishedSource(newsItem: T) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp),
    ) {
        Text(
            text = "${
                when (newsItem) {
                    is Article -> newsItem.source
                    is Docs -> newsItem.source
                    else -> "Unknown"
                }
            } • ${ when (newsItem) {
                is Article -> newsItem.subsection
                is Docs -> newsItem.subsection_name ?: ""
                else -> "Unknown"
            }}",
            fontSize = 10.sp
        )
        Spacer(
            modifier = Modifier
                .weight(1f)
                .width(0.dp)
        )
        when (newsItem) {
            is Article -> newsItem.published_date
            is Docs -> newsItem.pub_date
            else -> "Unknown"
        }.let {
            Text(
                text = it,
                fontSize = 10.sp
            )
        }

    }
}

