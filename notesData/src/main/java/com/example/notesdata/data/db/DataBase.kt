package com.example.notesdata.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [PostEntity::class, PostImageEntity::class, TagEntity::class, PostTagEntity::class, NewsEntity::class], version = 1)
abstract class PostDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao
    abstract fun postImageDao(): PostImageDao
    abstract fun tagDao(): TagDao
    abstract fun postTagDao(): PostTagDao
    abstract fun newsDao(): NewsDao

    companion object {
        @Volatile
        private var INSTANCE: PostDatabase? = null

        fun getDB(context: Context): PostDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PostDatabase::class.java,
                    "post_db"
                )
                    .addCallback(PostDataBaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class PostDataBaseCallback :Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            db.execSQL("INSERT INTO tags (name) VALUES ('Спорт'), ('Музыка'), ('Кино'), ('Образование'), ('Технологии'), ('PROD')")
        }
    }
}
