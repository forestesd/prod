package com.example.notesui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.notesdata.domain.models.NewsPostUi
import com.example.notesdata.data.NotesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    notesViewModel: NotesViewModel,
    onBack: () -> Unit,
    onNewsClicked: (NewsPostUi) -> Unit

){
    val posts by notesViewModel.allPosts.collectAsState()
    val favoritePosts =  posts.filter { it.isFavorite }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Избранное",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        onBack()
                    }
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Стрелка назад")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(Color.White)
        )
        LazyColumn(
            Modifier.fillMaxSize()
        ) {
            items(favoritePosts, key = {item -> item.postId}){item ->
                NotesCard(item, notesViewModel,onNewsClicked)
            }
        }
    }
}