package com.example.financedate

import androidx.compose.ui.graphics.Color
import com.example.financedate.db.GoalEntity
import org.w3c.dom.Comment

data class GoalWithProgress(
    val goal: GoalEntity,
    val progress: Float,
    val progressColor: Color
)
data class TransactionUi(
    val id: Int,
    val transactionType: String,
    val ammount: String,
    val goalName: String,
    val comments: String?
)
