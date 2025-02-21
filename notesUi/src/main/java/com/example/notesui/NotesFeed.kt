package com.example.notesui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.notesdata.NewsPostUi
import com.example.notesdata.NotesViewModel

@Composable
fun NotesFeed(
    notesViewModel: NotesViewModel,
    onNewsClicked: (NewsPostUi) -> Unit
) {
    val posts by notesViewModel.allPosts

    LaunchedEffect(posts) {
        notesViewModel.getAllNotes()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(posts) { item ->
            var expanded by remember { mutableStateOf(false) }

            val isFavorite = notesViewModel.favoritePosts[item.postId] ?: false

            LaunchedEffect(item.postId) {
                notesViewModel.checkFavorite(item.postId)
            }
            val fullText = item.content
            val showMoreText = " Ещё"
            val truncatedText = if (fullText.length > 100) fullText.take(120) + "..." else fullText

            val annotatedString = buildAnnotatedString {
                append(if (expanded) fullText else truncatedText)
                if (!expanded && fullText.length > truncatedText.length) {
                    withStyle(style = SpanStyle(color = Color.Blue)) {
                        append(showMoreText)
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                when (item.images.size) {
                    1 -> {
                        SingleImage(
                            item.images[0],
                            Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .padding(10.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                    }

                    2 -> {
                        TwoImages(
                            item.images,
                            Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .padding(10.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                    }

                    else -> {
                        Image(
                            painter = painterResource(R.drawable.placeholder),
                            contentDescription = "Placeholder",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .padding(10.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }


                Text(
                    text = annotatedString,
                    maxLines = if (expanded) Int.MAX_VALUE else 3,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(10.dp)
                        .clickable { expanded = !expanded }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FavoriteButton(isFavorite) { notesViewModel.toggleFavorite(item.postId) }
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(item.tags) {
                            Box(
                                modifier = Modifier
                                    .padding(vertical = 10.dp, horizontal = 10.dp)
                                    .height(25.dp)
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(Color.LightGray),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    modifier = Modifier.padding(horizontal = 10.dp),
                                    text = it
                                )
                            }

                        }


                    }
                }




                if (item.news != null && item.news?.newsUrl != "null") {
                    NewsTab(item.news, onNewsClicked)
                }

            }
        }
    }
}

@Composable
fun SingleImage(uri: String, modifier: Modifier) {
    AsyncImage(
        model = uri,
        contentDescription = "SecondImage",
        modifier = modifier,
        contentScale = ContentScale.Crop,
        placeholder = painterResource(R.drawable.placeholder),
        error = painterResource(R.drawable.placeholder)
    )
}


@Composable
fun TwoImages(uris: List<String>, modifier: Modifier) {
    Row(modifier = modifier) {
        uris.forEach { uri ->
            AsyncImage(
                model = uri,
                contentDescription = "Two Images",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .aspectRatio(1f),
                placeholder = painterResource(R.drawable.placeholder),
                error = painterResource(R.drawable.placeholder)
            )
        }
    }
}

@Composable
fun NewsTab(
    news: NewsPostUi?,
    onNewsClicked: (NewsPostUi) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFD7ADAD).copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    news?.let { onNewsClicked(it) }
                },
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = news!!.imageUrl,
                contentDescription = "NewsImage",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(10.dp)
                    .size(45.dp)
                    .clip(RoundedCornerShape(12.dp)),
                placeholder = painterResource(R.drawable.placeholder),
                error = painterResource(R.drawable.placeholder)
            )

            Text(
                text = news.title.take(25) + "...",
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            IconButton(
                onClick = {
                    onNewsClicked(news)
                }
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "BackIcon"
                )
            }
        }
    }

}

@Composable
fun FavoriteButton(
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit
) {
    IconButton(onClick = onToggleFavorite) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = "Favorite",
            tint = if (isFavorite) Color.Red else Color.Gray
        )
    }
}
