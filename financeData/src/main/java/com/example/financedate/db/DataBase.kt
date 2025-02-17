package com.example.financedate.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GoalEntity::class, TransactionEntity::class], version = 1)
abstract class FinanceDB: RoomDatabase(){

    abstract fun goalDao(): GoalDAO
    abstract fun transactionDao(): TransactionDao

    companion object{
        @Volatile
        private var INSTANСE: FinanceDB? = null

        fun getDB(context: Context): FinanceDB {
            return INSTANСE ?: synchronized(this){
                val instance= Room.databaseBuilder(
                    context.applicationContext,
                    FinanceDB::class.java,
                    "finance_db"
                ).build()
                INSTANСE = instance
                instance
            }
        }
    }
}