package com.example.financedate.domain.use_cases

import com.example.financedate.domain.repository.FinanceRepositoryInterface
import javax.inject.Inject

class AllAmountUseCase @Inject constructor(
    private val financeRepositoryInterface: FinanceRepositoryInterface
) {
    suspend operator fun invoke() = financeRepositoryInterface.allAmount()
}