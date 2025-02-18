package com.example.notesdata.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PostEntity::class, PostImageEntity::class, Tag::class, PostTagEntity::class], version = 1)
abstract class PostDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao
    abstract fun postImageDao(): PostImageDao
    abstract fun tagDao(): TagDao
    abstract fun postTagDao(): PostTagEntity

    companion object {
        @Volatile
        private var INSTANCE: PostDatabase? = null

        fun getDB(context: Context): PostDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PostDatabase::class.java,
                    "event_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}