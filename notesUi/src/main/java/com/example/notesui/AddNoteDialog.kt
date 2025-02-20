package com.example.notesui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.financeui.GoalDropDown
import com.example.financeui.PositiveAndNegativeButton
import com.example.notesdata.AddNoteViewModel
import com.example.notesdata.NotesViewModel
import com.example.notesdata.db.TagEntity


@Composable
fun AddNoteDialog(
    onBack: () -> Unit,
    onPickImage: () -> Unit,
    addNoteViewModel: AddNoteViewModel,
    notesViewModel: NotesViewModel
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        notesViewModel.getAllTags()
    }
    var noteContent by remember { mutableStateOf("") }
    var noteTags by remember { mutableStateOf("") }

    val selectedTags by addNoteViewModel.selectedTags

    val selectedImage by addNoteViewModel.selectedImage.collectAsState()

    Dialog(
        onDismissRequest = { onBack() },
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    text = "Новая заметкка",
                    style = MaterialTheme.typography.titleLarge
                )

                Button(
                    onClick = { onPickImage() }
                ) {
                    Text(
                        text = "Добавить картинку"
                    )
                }

                OutlinedTextField(
                    value = noteContent,
                    onValueChange = {
                        noteContent = it
                    },
                    label = { Text("Содержание") }
                )
                TagSelector(
                    notesViewModel = notesViewModel,
                    selectedTags = selectedTags,
                    onTagSelected = { addNoteViewModel.saveTags(it) }
                )
                if (selectedImage.isNotEmpty()) {
                    LazyRow {
                        items(selectedImage) { uri ->
                            AsyncImage(
                                model = uri,
                                contentDescription = "Выбранное изображение",
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(4.dp),
                            )
                        }
                    }
                }
                PositiveAndNegativeButton(
                    onBack = {onBack()},
                    onSave = {
                        addNoteViewModel.saveNoteContent(noteContent)
                        addNoteViewModel.saveNote(context)
                        onBack()

                    },
                    text = "Сохранить"
                )

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
    Column {
        Text("Выберите теги", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        LazyColumn {
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
                        modifier = Modifier.padding(start = 8.dp),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}