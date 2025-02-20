package com.example.notesui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notesdata.NewsPostUi
import com.example.notesdata.NotesViewModel

@Composable
fun NotesMainScreen(
    notesViewModel: NotesViewModel,
    onAddButtonClicked: () -> Unit,
    onNewsClicked: (NewsPostUi) -> Unit
) {



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



        AddNote(onAddNote = onAddButtonClicked)

        NotesFeed(notesViewModel, onNewsClicked)

    }
}

