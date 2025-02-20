package com.example.notesui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.notesdata.NotesViewModel

@Composable
fun NotesFeed(
    notesViewModel: NotesViewModel
){
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items()
    }
}