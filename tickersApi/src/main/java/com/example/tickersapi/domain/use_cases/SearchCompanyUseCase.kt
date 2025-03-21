package com.example.tickersapi.domain.use_cases

import com.example.tickersapi.domain.repository.TickersRepositoryInterface
import javax.inject.Inject

class SearchCompanyUseCase @Inject constructor(
    private val tickersRepositoryInterface: TickersRepositoryInterface
) {
    suspend operator fun invoke(apiKey: String, q: String, exchange: String) =
        tickersRepositoryInterface.searchCompany(
            apiKey = apiKey,
            q = q,
            exchange = exchange
        )
}