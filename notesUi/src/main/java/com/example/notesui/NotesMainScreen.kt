package com.example.notesui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.notesdata.AddNoteViewModel
import com.example.notesdata.NotesViewModel

@Composable
fun NotesMainScreen(
    notesViewModel: NotesViewModel,
    addNoteViewModel: AddNoteViewModel,
    onPickImageClick: () -> Unit,
) {
    LaunchedEffect(Unit) {
        notesViewModel.getAllImages()
    }

    var showAddNoteDialog by remember { mutableStateOf(false) }
    val image by notesViewModel.allImages
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Лента",
            fontSize = 34.sp,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 10.dp, top = 16.dp)
        )



        AddNote(onAddNote = { showAddNoteDialog = true })
        if (showAddNoteDialog) {
            AddNoteDialog(
                onBack = { showAddNoteDialog = false },
                onPickImage = onPickImageClick,
                addNoteViewModel = addNoteViewModel,
                notesViewModel = notesViewModel
            )
        }
        LazyColumn {
            items(image) { uri ->
                AsyncImage(
                    model = uri.photoUrl,
                    contentScale = ContentScale.Crop,
                    contentDescription = "Выбранное изображение",
                    modifier = Modifier
                        .size(450.dp)
                        .padding(4.dp),
                )
            }
        }

    }
}

