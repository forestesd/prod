package com.example.financedate.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.math.BigDecimal

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val totalCoastTarget: BigDecimal,
    val currentCollected: BigDecimal =  BigDecimal.ZERO,
    val deadLine: String? =null,
    val status: String = "active"
)

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val goalName: String,
    val amount: BigDecimal,
    val type: String, // пополнение/снятие
    val date: String,
    val comment: String? = null

)

class BigDecimalConverter {

    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toBigDecimal(value: String?): BigDecimal? {
        return value?.let { BigDecimal(it) }
    }
}