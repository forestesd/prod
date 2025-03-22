package com.example.notesdata.data.repository

import com.example.notesdata.data.db.NewsDao
import com.example.notesdata.data.db.PostDao
import com.example.notesdata.data.db.PostEntity
import com.example.notesdata.data.db.PostImageDao
import com.example.notesdata.data.db.PostTagDao
import com.example.notesdata.data.db.TagDao
import com.example.notesdata.data.db.TagEntity
import com.example.notesdata.data.utils.postMapperUi
import com.example.notesdata.domain.models.PostUi
import com.example.notesdata.domain.repository.NoteRepositoryInterface
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NotesRepository @Inject constructor(
    private val postDao: PostDao,
    private val postImageDao: PostImageDao,
    private val tagDao: TagDao,
    private val postTagDao: PostTagDao,
    private val newsDao: NewsDao
) : NoteRepositoryInterface {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getAllNotes() = postDao.getAllPosts()
        .flatMapLatest { allPost ->
            flow {
                val posts = allPost.map { post ->
                    val images = postImageDao.getImageByEventId(post.id)
                    val tags = postTagDao.getTagsForPost(post.id)
                    val allPostTag = tags.map { tagDao.getTagById(it.tagId) }
                    val news = post.newsId?.let { newsDao.getNewsById(it) }

                    postMapperUi(post = post, image = images, tags = allPostTag, news = news)
                }
                emit(posts)
            }
        }

    override suspend fun getAllTags() = tagDao.getAllTags()

    override suspend fun checkFavorite(id: Long) = postDao.getPostById(id)

    override suspend fun toggleFavorite(id: Long, isFavorite: Boolean) =
        postDao.updatePostFavoriteStatus(id, isFavorite)

}