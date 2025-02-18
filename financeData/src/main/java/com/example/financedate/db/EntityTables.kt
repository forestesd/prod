package com.example.financedate.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val totalCoastTarget: Double,
    val currentCollected: Double = 0.0,
    val deadLine: String? =null,
    val status: String = "active"
)

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val goalName: String,
    val amount: Double,
    val type: String, // пополнение/снятие
    val date: String,
    val comment: String? = null

)