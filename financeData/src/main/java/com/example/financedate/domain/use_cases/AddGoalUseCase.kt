package com.example.financedate.domain.use_cases

import com.example.financedate.domain.repository.FinanceRepositoryInterface
import java.math.BigDecimal
import javax.inject.Inject

class AddGoalUseCase @Inject constructor(
    private val financeRepositoryInterface: FinanceRepositoryInterface
) {
    suspend operator fun invoke(name: String, targetCost: BigDecimal, deadLine: String?) =
        financeRepositoryInterface.addGoal(
            name = name,
            targetCost = targetCost,
            deadLine = deadLine
        )
}