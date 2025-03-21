package com.example.tickersapi.domain.use_cases

import com.example.tickersapi.domain.repository.TickersRepositoryInterface
import javax.inject.Inject

class GetCompanyInfoUseCase @Inject constructor(
    private val tickersRepositoryInterface: TickersRepositoryInterface
) {
    suspend operator fun invoke(apiKey: String) = tickersRepositoryInterface.getCompanyInfo(apiKey)
}