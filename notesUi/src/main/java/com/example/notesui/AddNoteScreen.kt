package com.example.notesui

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.notesdata.AddNoteViewModel
import com.example.notesdata.NotesViewModel
import com.example.notesdata.db.NewsEntity
import com.example.notesdata.db.TagEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    onBack: () -> Unit,
    onPickImage: () -> Unit,
    addNoteViewModel: AddNoteViewModel,
    notesViewModel: NotesViewModel,
    news: NewsEntity? = null
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        notesViewModel.getAllTags()
    }
    var noteContent by remember { mutableStateOf("") }
    var isContentValid by remember { mutableStateOf(true) }

    val selectedTags by addNoteViewModel.selectedTags

    val selectedImage by addNoteViewModel.selectedImage.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Новая заметкка",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        onBack()
                    addNoteViewModel.clearData()
                    }
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Стрелка назад")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(Color.White)
        )

        PickImages(selectedImage, onPickImage = onPickImage)
        if (!isContentValid){
            Text(
                text = "Введите текст заметки!!!",
                fontSize = 16.sp,
                color = Color.Red
            )
        }
        OutlinedTextField(
            value = noteContent,
            onValueChange = {
                noteContent = it
                isContentValid = it.isNotBlank()
            },
            label = { Text("Содержание") },
            modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp, max = 150.dp).padding(horizontal = 16.dp)
        )

        TagSelector(
            notesViewModel = notesViewModel,
            selectedTags = selectedTags,
            onTagSelected = { addNoteViewModel.saveTags(it) }
        )
        Button(
            onClick = {
                addNoteViewModel.saveNoteContent(noteContent)
                addNoteViewModel.saveNote(context, news = news)
                onBack()
                notesViewModel.getAllNotes()
            },
            enabled = noteContent != " " && noteContent.isNotEmpty()
        ) {
            Text(
                text = "Сохранить заметку"
            )
        }
    }

}

@Composable
fun PickImages(
    selectedImage: List<Uri>,
    onPickImage: () -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(25.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        items(selectedImage) { uri ->
            AsyncImage(
                model = uri,
                contentDescription = "Выбранное изображение",
                modifier = Modifier
                    .size(200.dp)
                    .padding(4.dp),
                contentScale = ContentScale.Crop
            )
        }
        item {
            Button(
                onClick = { onPickImage() },
                modifier = Modifier.size(200.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Black,
                    containerColor = Color(0xFFCAADFF).copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(16.dp),
                enabled = selectedImage.size < 2
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить изображение")
            }
        }
    }
}


@Composable
fun TagSelector(
    selectedTags: List<TagEntity>,
    notesViewModel: NotesViewModel,
    onTagSelected: (List<TagEntity>) -> Unit
) {

    Text(
        text = "Выберите теги",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(10.dp)

    )

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.6f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        items(notesViewModel.allTags.value) { tag ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val isChecked = selectedTags.contains(tag)

                Checkbox(
                    checked = isChecked,
                    onCheckedChange = {
                        val updatedTags = if (isChecked) {
                            selectedTags - tag
                        } else {
                            selectedTags + tag
                        }
                        onTagSelected(updatedTags)
                    }
                )
                Text(
                    text = tag.name,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable {
                            val updatedTags = if (isChecked) {
                                selectedTags - tag
                            } else {
                                selectedTags + tag
                            }
                            onTagSelected(updatedTags)
                        },
                    fontSize = 16.sp,
                )
            }
        }
    }
}
