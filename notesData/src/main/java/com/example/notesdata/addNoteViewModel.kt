package com.example.notesdata

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesdata.db.PostDao
import com.example.notesdata.db.PostEntity
import com.example.notesdata.db.PostImageDao
import com.example.notesdata.db.PostImageEntity
import com.example.notesdata.db.PostTagDao
import com.example.notesdata.db.PostTagEntity
import com.example.notesdata.db.TagDao
import com.example.notesdata.db.TagEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject

class AddNoteViewModel @Inject constructor(
    application: Application,
    private val postDao: PostDao,
    private val postImageDao: PostImageDao,
    private val tagDao: TagDao,
    private val postTagDao: PostTagDao
) : AndroidViewModel(application) {
    private val _selectedImage = MutableStateFlow<List<Uri>>(emptyList())
    val selectedImage: StateFlow<List<Uri>> get() = _selectedImage

    private val _selectedTags = mutableStateOf<List<TagEntity>>(emptyList())
    val selectedTags: State<List<TagEntity>> = _selectedTags

    private val _noteContent = mutableStateOf<String>("")
    val noteContent: State<String> = _noteContent

    fun saveImageUri(uris: List<Uri>) {
        _selectedImage.value = uris
    }

    fun saveTags(tags: List<TagEntity>) {
        _selectedTags.value = tags
    }

    fun saveNoteContent(content: String) {
        _noteContent.value = content
    }

    fun saveNote(context: Context) {
        viewModelScope.launch {
            if (_noteContent.value.isNotBlank()) {
                val noteId = postDao.insertPost(PostEntity(content = _noteContent.value))
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
                val images = postImageDao.getImageByEventId(noteId)
                Log.i("Info", images.toString())
                _selectedTags.value.forEach { tag ->
                    postTagDao.insertPostTag(PostTagEntity(postId = noteId, tagId = tag.id))
                }
            }
        }
    }

    private fun saveImageToAppDirectory(context: Context, imageUri: Uri): Uri? {
        val fileName = "image_${System.currentTimeMillis()}.jpg"


        val appContext = context.applicationContext
        val contentResolver = appContext.contentResolver


        val compressedUri = compressImage(context, imageUri, quality = 1)


        return compressedUri?.let {
            val inputStream = contentResolver.openInputStream(it)
            val outputStream = FileOutputStream(File(appContext.filesDir, fileName))

            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()

            Uri.fromFile(File(appContext.filesDir, fileName))
        }
    }


    private fun compressImage(context: Context, imageUri: Uri, quality: Int = 80): Uri? {
        val contentResolver = context.contentResolver
        val inputStream: InputStream? = contentResolver.openInputStream(imageUri)


        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
            BitmapFactory.decodeStream(inputStream, null, this)

            val scale = calculateInSampleSize(this, 800, 800)
            inSampleSize = scale
            inJustDecodeBounds = false
        }


        val bitmap: Bitmap? = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri), null, options)


        val fileName = "compressed_image_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)
        val outputStream = FileOutputStream(file)

        bitmap?.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        outputStream.flush()
        outputStream.close()

        return Uri.fromFile(file)
    }


    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {

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
