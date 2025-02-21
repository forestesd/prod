package com.example.notesdata

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesdata.db.NewsDao
import com.example.notesdata.db.PostDao
import com.example.notesdata.db.PostImageDao
import com.example.notesdata.db.PostTagDao
import com.example.notesdata.db.TagDao
import com.example.notesdata.db.TagEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class NotesViewModel @Inject constructor(
    application: Application,
    private val postDao: PostDao,
    private val postImageDao: PostImageDao,
    private val tagDao: TagDao,
    private val postTagDao: PostTagDao,
    private val newsDao: NewsDao

) : AndroidViewModel(application) {

    private val _allTags = mutableStateOf<List<TagEntity>>(emptyList())
    val allTags: State<List<TagEntity>> = _allTags


    private val _allPosts = MutableStateFlow<List<PostUi>>(emptyList())
    val allPosts: StateFlow<List<PostUi>> = _allPosts

    private val _favoritePosts = mutableStateMapOf<Long, Boolean>()
    val favoritePosts: Map<Long, Boolean> get() = _favoritePosts



    fun getAllTags() {
        viewModelScope.launch {
            _allTags.value = tagDao.getAllTags()
        }
    }

    fun checkFavorite(id: Long) {
        viewModelScope.launch {
            val post = postDao.getPostById(id)
            _favoritePosts[id] = post?.ifFavorites ?: false
        }
    }

    fun toggleFavorite(id: Long) {
        viewModelScope.launch {
            val currentState = _favoritePosts[id] ?: false
            val newState = !currentState
            postDao.updatePostFavoriteStatus(id, newState)
            _favoritePosts[id] = newState
        }
    }

    fun getAllNotes(){
        viewModelScope.launch {
            val posts = mutableListOf<PostUi>()
            val allPost = postDao.getAllPosts()
            allPost.forEach{ post ->
                val images = postImageDao.getImageByEventId(post.id)
                val tags = postTagDao.getTagsForPost(post.id)
                val allPostTag = tags.map { tagDao.getTagById(it.tagId) }
                _favoritePosts[post.id] = post.ifFavorites
                val news = if (post.newsId != null) newsDao.getNewsById(post.newsId) else null
             posts.add(postMapperUi(post =  post, image =  images, tags = allPostTag, news = news))
            }
            _allPosts.emit(posts)
        }
    }

}