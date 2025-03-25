package com.example.financedate.domain.use_cases

import com.example.financedate.data.db.GoalEntity
import com.example.financedate.domain.repository.FinanceRepositoryInterface
import javax.inject.Inject

class DeleteGoalUseCase @Inject constructor(
    private val financeRepositoryInterface: FinanceRepositoryInterface
) {
    suspend operator fun invoke(goal: GoalEntity) = financeRepositoryInterface.deleteGoal(goal)
}