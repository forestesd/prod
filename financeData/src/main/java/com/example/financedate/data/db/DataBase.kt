package com.example.financedate.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [GoalEntity::class, TransactionEntity::class], version = 1)
@TypeConverters(BigDecimalConverter::class)
abstract class FinanceDB: RoomDatabase(){

    abstract fun goalDao(): GoalDAO
    abstract fun transactionDao(): TransactionDao

    companion object{
        @Volatile
        private var INSTANCE: FinanceDB? = null

        fun getDB(context: Context): FinanceDB {
            return INSTANCE ?: synchronized(this){
                val instance= Room.databaseBuilder(
                    context.applicationContext,
                    FinanceDB::class.java,
                    "finance_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}