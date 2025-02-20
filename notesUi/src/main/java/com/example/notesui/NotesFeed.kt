package com.example.notesui

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.notesdata.NotesViewModel

@Composable
fun NotesFeed(
    notesViewModel: NotesViewModel
) {
    LaunchedEffect(Unit) {
        notesViewModel.getAllNotes()
    }
    val posts by notesViewModel.allPosts
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(posts) { item ->
            var expanded by remember { mutableStateOf(false) }

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
                            contentDescription = "Palceholder",
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
        }
    }
}

@Composable
fun SingleImage(uri: String, modifier: Modifier) {
    AsyncImage(
        model = uri,
        contentDescription = "SecondImge",
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

