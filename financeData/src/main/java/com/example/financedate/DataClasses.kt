package com.example.financedate

import com.example.financedate.db.GoalEntity

data class GoalWithProgress(
    val goal: GoalEntity,
    val progress: Float
)
