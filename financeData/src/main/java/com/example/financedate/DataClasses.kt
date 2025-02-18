package com.example.financedate

import androidx.compose.ui.graphics.Color
import com.example.financedate.db.GoalEntity

data class GoalWithProgress(
    val goal: GoalEntity,
    val progress: Float,
    val progressColor: Color
)
data class TransactionUi(
    val id: Int,
    val transactionType: String,
    val amount: String,
    val goalName: String,
    val comments: String?
)

data class AmountUi(
    val totalAmount: String,
    val goalComplete: String,
    val depositMonths: String,
    val goalsComplete: String
)
