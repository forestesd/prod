package com.example.notesdata.data

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesdata.domain.models.PostUi
import com.example.notesdata.data.db.NewsDao
import com.example.notesdata.data.db.PostDao
import com.example.notesdata.data.db.PostImageDao
import com.example.notesdata.data.db.PostTagDao
import com.example.notesdata.data.db.TagDao
import com.example.notesdata.data.db.TagEntity
import com.example.notesdata.data.utils.postMapperUi
import com.example.notesdata.domain.use_cases.CheckFavoritesUseCase
import com.example.notesdata.domain.use_cases.GetAllNotesUseCase
import com.example.notesdata.domain.use_cases.GetAllTagsUseCase
import com.example.notesdata.domain.use_cases.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class NotesViewModel @Inject constructor(
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val getAllTagsUseCase: GetAllTagsUseCase,
    private val checkFavoritesUseCase: CheckFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _allTags = MutableStateFlow<List<TagEntity>>(emptyList())
    val allTags: StateFlow<List<TagEntity>> = _allTags

    private val _allPosts = MutableStateFlow<List<PostUi>>(emptyList())
    val allPosts: StateFlow<List<PostUi>> = _allPosts


    private val _favoritePosts = mutableStateMapOf<Long, Boolean>()
    val favoritePosts: Map<Long, Boolean> get() = _favoritePosts


    fun getAllTags() {
        viewModelScope.launch {
            _allTags.value = getAllTagsUseCase.invoke()
        }
    }

    fun checkFavorite(id: Long) {
        viewModelScope.launch {
            val post = checkFavoritesUseCase.invoke(id)
            _favoritePosts[id] = post?.ifFavorites ?: false
        }
    }

    fun toggleFavorite(id: Long) {
        viewModelScope.launch {
            val currentState = _favoritePosts[id] ?: false
            val newState = !currentState
            toggleFavoriteUseCase.invoke(id, newState)
            _favoritePosts[id] = newState
        }
    }

    fun getAllNotes() {
        viewModelScope.launch {
            getAllNotesUseCase.invoke().collect { posts ->
                _allPosts.value = posts
            }
        }
    }

}