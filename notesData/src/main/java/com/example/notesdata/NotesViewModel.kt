package com.example.notesdata

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesdata.db.NewsDao
import com.example.notesdata.db.PostDao
import com.example.notesdata.db.PostImageDao
import com.example.notesdata.db.PostTagDao
import com.example.notesdata.db.TagDao
import com.example.notesdata.db.TagEntity
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


    private val _allPosts = mutableStateOf<List<PostUi>>(emptyList())
    val allPosts: State<List<PostUi>> = _allPosts


    fun getAllTags() {
        viewModelScope.launch {
            _allTags.value = tagDao.getAllTags()
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
                val news = if (post.newsId != null) newsDao.getNewsById(post.newsId) else null
             posts.add(postMapperUi(post =  post, image =  images, tags = allPostTag, news = news))
            }
            _allPosts.value = posts
        }
    }

}