package com.example.notesdata.data

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesdata.data.db.NewsDao
import com.example.notesdata.data.db.NewsEntity
import com.example.notesdata.data.db.PostDao
import com.example.notesdata.data.db.PostEntity
import com.example.notesdata.data.db.PostImageEntity
import com.example.notesdata.data.db.PostTagEntity
import com.example.notesdata.data.db.TagEntity
import com.example.notesdata.domain.use_cases.SaveNoteUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class AddNoteViewModel @Inject constructor(
    private val saveNoteUseCase: SaveNoteUseCase
) : ViewModel() {
    private val _selectedImage = MutableStateFlow<List<Uri>>(emptyList())
    val selectedImage: StateFlow<List<Uri>> get() = _selectedImage

    private val _selectedTags = mutableStateOf<List<TagEntity>>(emptyList())
    val selectedTags: State<List<TagEntity>> = _selectedTags

    private val _noteContent = mutableStateOf("")

    fun saveImageUri(uris: List<Uri>) {
        _selectedImage.value += uris
    }

    fun saveTags(tags: List<TagEntity>) {
        _selectedTags.value = tags
    }

    fun saveNoteContent(content: String) {
        _noteContent.value = content
    }

    fun saveNote(context: Context, news: NewsEntity?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (_noteContent.value.isNotBlank()) {
                saveNoteUseCase.invoke(
                    context = context,
                    news = news,
                    noteContent = _noteContent.value,
                    selectedImage = _selectedImage.value,
                    selectedTags = _selectedTags.value
                )
            }

            clearData()
        }

    }

    fun clearData() {
        _selectedImage.value = emptyList()
        _selectedTags.value = emptyList()
        _noteContent.value = ""
    }

}
