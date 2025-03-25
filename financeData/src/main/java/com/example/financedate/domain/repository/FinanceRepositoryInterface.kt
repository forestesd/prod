package com.example.financedate.domain.repository

import com.example.financedate.data.db.GoalEntity
import com.example.financedate.domain.models.AmountUi
import com.example.financedate.domain.models.GoalWithProgress
import com.example.financedate.domain.models.TransactionUi
import java.math.BigDecimal

interface FinanceRepositoryInterface {

    suspend fun addGoal(name: String, targetCost: BigDecimal, deadLine: String?)

    suspend fun getGoalProgress(): List<GoalWithProgress>

    suspend fun deleteGoal(goal: GoalEntity)

    suspend fun addTransaction(
        goalName: String,
        amount: BigDecimal,
        type: String,
        comment: String?
    )

    suspend fun getTransactions(): List<TransactionUi>

    suspend fun allAmount(): AmountUi
}