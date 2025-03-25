package com.example.financedate.domain.use_cases

import com.example.financedate.domain.repository.FinanceRepositoryInterface
import java.math.BigDecimal
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val financeRepositoryInterface: FinanceRepositoryInterface
) {
    suspend operator fun invoke(
        goalName: String,
        amount: BigDecimal,
        type: String,
        comment: String?
    ) = financeRepositoryInterface.addTransaction(
        goalName = goalName,
        amount = amount,
        type = type,
        comment = comment
    )
}