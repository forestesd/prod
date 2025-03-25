package com.example.financedate.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface GoalDAO{
    @Insert
    suspend fun insert(goal: GoalEntity): Long

    @Query("SELECT * from goals")
    suspend fun getAllGoals(): List<GoalEntity>

    @Query("SELECT * from goals WHERE id = :goalId Limit 1")
    suspend fun getGoalById(goalId: Int): GoalEntity

    @Query("SELECT * from goals WHERE name = :goalName Limit 1")
    suspend fun getGoalByName(goalName:String): GoalEntity
    @Update
    suspend fun update(goal: GoalEntity)

    @Delete
    suspend fun delete(goal: GoalEntity)
}

@Dao
interface TransactionDao {
    @Insert
    suspend fun insert(transaction: TransactionEntity): Long

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    suspend fun getAllTransactions(): List<TransactionEntity>

    @Query("DELETE FROM transactions WHERE goalName = :goalName")
    suspend fun deleteTransactionsForGoal(goalName:String )
}