package com.example.home

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.apis.Article
import com.example.apis.NewsViewModel

@Composable
fun NewsFeedMain(news: List<Article>, viewModel: NewsViewModel) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp),

        ) {
        items(news, key = { item -> item.title}) { item ->
            val imageUrl = viewModel.getImageUrlForArticle(item)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(12.dp),
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
    }
}


@Composable
fun PublishedSource(newsItem: Article) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp),
    ) {
        Text(
            text = "${newsItem.source} • ${newsItem.subsection}",
            fontSize = 10.sp
        )
        Spacer(
            modifier = Modifier
                .weight(1f)
                .width(0.dp)
        )
        Text(
            text = newsItem.published_date,
            fontSize = 10.sp
        )

    }
}

@Composable
fun NewsMain(newsItem: Article, imageUrl: String?) {
    Image(
        painter = rememberAsyncImagePainter(imageUrl ?: R.drawable.placeholder),
        modifier = Modifier
            .fillMaxWidth()
            .height(PxToDp(400f))
            .clip(RoundedCornerShape(12.dp)),
        contentScale = ContentScale.Crop,
        contentDescription = "News Image"
    )

    Text(
        text = newsItem.title,
        modifier = Modifier.padding(top = 10.dp),
        fontSize = 16.sp,

        )

    Text(
        text = newsItem.abstract,
        modifier = Modifier.padding(top = 12.dp),
        fontSize = 12.sp
    )
}