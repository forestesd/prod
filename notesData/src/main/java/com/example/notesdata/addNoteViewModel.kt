package com.example.notesdata

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesdata.db.NewsDao
import com.example.notesdata.db.NewsEntity
import com.example.notesdata.db.PostDao
import com.example.notesdata.db.PostEntity
import com.example.notesdata.db.PostImageDao
import com.example.notesdata.db.PostImageEntity
import com.example.notesdata.db.PostTagDao
import com.example.notesdata.db.PostTagEntity
import com.example.notesdata.db.TagEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class AddNoteViewModel @Inject constructor(
    application: Application,
    private val postDao: PostDao,
    private val postImageDao: PostImageDao,
    private val postTagDao: PostTagDao,
    private val newsDao: NewsDao
) : AndroidViewModel(application) {
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
        viewModelScope.launch {
            if (_noteContent.value.isNotBlank()) {
                if (news != null) newsDao.insertNews(news)
                val newsId = if (news != null) newsDao.getNewsByUrl(news.articleUrl).id else null
                val noteId = postDao.insertPost(
                    PostEntity(
                        content = _noteContent.value,
                        createdAt = LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")),
                        newsId = newsId
                    )
                )
                if (news != null) newsDao.insertNews(news)

                _selectedImage.value.forEach { uri ->
                    postImageDao.insertImage(
                        PostImageEntity(
                            postId = noteId, photoUrl = saveImageToAppDirectory(
                                imageUri = uri,
                                context = context
                            ).toString()
                        )
                    )
                }
                _selectedTags.value.forEach { tag ->
                    postTagDao.insertPostTag(PostTagEntity(postId = noteId, tagId = tag.id))
                }
            }
            clearData()
        }
    }

    fun clearData() {
        _selectedImage.value = emptyList()
        _selectedTags.value = emptyList()
        _noteContent.value = ""
    }

    private fun saveImageToAppDirectory(context: Context, imageUri: Uri): Uri? {
        val fileName = "image_${System.currentTimeMillis()}.jpg"


        val appContext = context.applicationContext
        val contentResolver = appContext.contentResolver

        val rotatedUri = rotateImageIfNeeded(context, imageUri)

        val compressedUri = compressImage(context, rotatedUri)

        return compressedUri?.let {
            val inputStream = contentResolver.openInputStream(it)

            val file = File(appContext.getExternalFilesDir(null), fileName)

            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()

            Uri.fromFile(file)
        }
    }

    private fun rotateImageIfNeeded(context: Context, imageUri: Uri): Uri {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)

        val matrix = Matrix()
        val orientation = getImageOrientation(imageUri, context)
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            matrix.postRotate(90f)
        }

        val rotatedBitmap =
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

        val fileName = "rotated_${System.currentTimeMillis()}.jpg"
        val file = File(context.getExternalFilesDir(null), fileName)
        val outputStream = FileOutputStream(file)

        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        return Uri.fromFile(file)
    }

    private fun getImageOrientation(imageUri: Uri, context: Context): Int {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }

        BitmapFactory.decodeStream(inputStream, null, options)

        return if (options.outWidth > options.outHeight) {
            Configuration.ORIENTATION_LANDSCAPE
        } else {
            Configuration.ORIENTATION_PORTRAIT
        }
    }

    private fun compressImage(context: Context, imageUri: Uri): Uri? {
        val quality = 40
        val contentResolver = context.contentResolver
        val inputStream: InputStream? = contentResolver.openInputStream(imageUri)

        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
            BitmapFactory.decodeStream(inputStream, null, this)

            val scale = calculateInSampleSize(this)
            inSampleSize = scale
            inJustDecodeBounds = false
        }


        val bitmap: Bitmap? =
            BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri), null, options)


        val fileName = "compressed_image_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)
        val outputStream = FileOutputStream(file)

        bitmap?.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        outputStream.flush()
        outputStream.close()

        return Uri.fromFile(file)
    }


    private fun calculateInSampleSize(
        options: BitmapFactory.Options,

        ): Int {
        val reqWidth = 800
        val reqHeight = 800
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

}
