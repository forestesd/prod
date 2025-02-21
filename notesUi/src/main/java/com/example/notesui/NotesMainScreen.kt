package com.example.notesui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notesdata.NewsPostUi
import com.example.notesdata.NotesViewModel

@Composable
fun NotesMainScreen(
    notesViewModel: NotesViewModel,
    onAddButtonClicked: () -> Unit,
    onNewsClicked: (NewsPostUi) -> Unit,
    onFavoritesClicked: () -> Unit
) {
    val posts by notesViewModel.allPosts
    val favoritePosts =  posts.filter { it.isFavorite }
    LaunchedEffect(Unit) {
        notesViewModel.getAllNotes()
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "Лента",
                fontSize = 34.sp,
                modifier = Modifier
                    .padding(start = 10.dp, top = 16.dp)
            )
            Spacer(Modifier.weight(1f))

            TextButton(
                onClick = { onFavoritesClicked()},
                modifier = Modifier.padding(top = 16.dp),
                enabled = favoritePosts.isNotEmpty()
            ) {
                Text(
                    text = "Избранное",
                    fontSize = 18.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(end = 10.dp)
                )
                Icon(
                    imageVector = if (favoritePosts.isNotEmpty()) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (favoritePosts.isNotEmpty()) Color.Red else Color.Gray
                )
            }
        }




        AddNote(onAddNote = onAddButtonClicked)

        NotesFeed(notesViewModel, onNewsClicked)

    }
}

