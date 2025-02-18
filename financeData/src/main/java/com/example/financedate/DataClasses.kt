package com.example.financedate

import androidx.compose.ui.graphics.Color
import com.example.financedate.db.GoalEntity

data class GoalWithProgress(
    val goal: GoalEntity,
    val progress: Float,
    val progressColor: Color
)
