package com.example.notesdata

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesdata.db.PostDao
import com.example.notesdata.db.PostImageDao
import com.example.notesdata.db.PostImageEntity
import com.example.notesdata.db.PostTagDao
import com.example.notesdata.db.TagDao
import com.example.notesdata.db.TagEntitiy
import kotlinx.coroutines.launch
import javax.inject.Inject

class NotesViewModel @Inject constructor(
    application: Application,
    private val postDao: PostDao,
    private val postImageDao: PostImageDao,
    private val tagDao: TagDao,
    private val postTagDao: PostTagDao

) : AndroidViewModel(application) {

    private val _allTags = mutableStateOf<List<TagEntitiy>>(emptyList())
    val allTags: State<List<TagEntitiy>> = _allTags

    fun saveImageUri(uri: String) {
        viewModelScope.launch {
            val photo = PostImageEntity(eventId = 1, photoUrl = uri)
            postImageDao.insertPhoto(photo)
        }
    }

    fun getAllTags() {
        viewModelScope.launch {
            _allTags.value = tagDao.getAllTags()
        }

    }
}