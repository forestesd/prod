package com.example.home.newsFeed

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.apis.domain.models.Article
import com.example.apis.data.NewsViewModel
import com.example.home.PxToDp
import com.example.home.R
@Composable
fun NewsCard(item: Article, viewModel: NewsViewModel, onCardClicked: (String) -> Unit) {
    val imageUrl = viewModel.getImageUrlForArticle(item)
    val scale = remember { Animatable(0.9f) }
    val alpha = remember { Animatable(0.0f) }

    LaunchedEffect(key1 = true) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500)
        )

        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 300)
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .alpha(alpha.value)
            .scale(scale.value),
        shape = RoundedCornerShape(12.dp),
        onClick = {
            onCardClicked(
                item.url
            )
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
fun NewsMain(newsItem: Article, imageUrl: String?) {

    AsyncImage(
        model = imageUrl,
        modifier = Modifier
            .fillMaxWidth()
            .height(PxToDp(400f))
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Gray),
        contentScale = ContentScale.Crop,
        placeholder = painterResource(R.drawable.placeholder),
        error = painterResource(R.drawable.placeholder),
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

