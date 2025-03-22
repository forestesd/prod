package com.example.notesdata.data.repository

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import com.example.notesdata.data.db.NewsDao
import com.example.notesdata.data.db.NewsEntity
import com.example.notesdata.data.db.PostDao
import com.example.notesdata.data.db.PostEntity
import com.example.notesdata.data.db.PostImageEntity
import com.example.notesdata.data.db.PostTagEntity
import com.example.notesdata.data.db.TagEntity
import com.example.notesdata.domain.repository.AddNoteRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class AddNoteRepository @Inject constructor(
    private val postDao: PostDao,
    private val newsDao: NewsDao
) : AddNoteRepositoryInterface {

    override suspend fun saveNote(
        context: Context,
        news: NewsEntity?,
        noteContent: String,
        selectedImage: List<Uri>,
        selectedTags: List<TagEntity>
    ) {
        if (news != null) newsDao.insertNews(news)
        val newsId = news?.let { newsDao.getNewsByUrl(it.articleUrl).id }

        val post = PostEntity(
            content = noteContent,
            createdAt = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")),
            newsId = newsId
        )

        val images = selectedImage.mapNotNull { uri ->
            saveImageToAppDirectory(context, uri)?.let {
                PostImageEntity(photoUrl = it.toString(), postId = post.id)
            }
        }

        val tags = selectedTags.map { tag ->
            PostTagEntity(tagId = tag.id, postId = post.id)
        }
        postDao.insertPostWithImagesAndTags(post, images, tags)
    }

    override suspend fun saveImageToAppDirectory(context: Context, imageUri: Uri): Uri? {
        val fileName = "image_${System.currentTimeMillis()}.jpg"

        return try {
            val rotatedUri = rotateImageIfNeeded(context, imageUri)
            val compressedUri = compressImage(context, rotatedUri)

            compressedUri?.let { uri ->
                val inputStream = context.contentResolver.openInputStream(uri)
                val externalFilesDir = context.getExternalFilesDir(null)

                if (externalFilesDir != null) {
                    val file = File(externalFilesDir, fileName)
                    val outputStream = FileOutputStream(file)

                    inputStream?.use { input ->
                        outputStream.use { output ->
                            input.copyTo(output)
                        }
                    }

                    Uri.fromFile(file)
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    private suspend fun rotateImageIfNeeded(context: Context, imageUri: Uri): Uri =
        withContext(Dispatchers.IO) {
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

            Uri.fromFile(file)
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