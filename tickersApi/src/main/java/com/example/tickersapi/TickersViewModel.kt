package com.example.tickersapi

import android.util.Log

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class TickersViewModel @Inject constructor(
    private val repository: TickersRepository,
    private val webSocket: TickersWebSocket
) : ViewModel() {
    private val _tickers = MutableStateFlow<List<TickerUi>>(emptyList())
    val tickers: StateFlow<List<TickerUi>> get() = _tickers

    private var _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching


    init {
        viewModelScope.launch {
            repository.webSocketOpen()
        }
    }

    private fun updateTickerData(symbol: String, newPrice: Double) {
        val updatedTickers = _tickers.value.toMutableList()

        val ticker = updatedTickers.find { it.symbol == symbol }

        if (ticker != null) {
            val updatedTicker = ticker.copy(
                price = newPrice.toString(),
                priceColor = if (newPrice > ticker.price.toDouble()) Color(0xFF1BBE31) else Color.Red
            )

            val index = updatedTickers.indexOf(ticker)
            updatedTickers[index] = updatedTicker
        }

        _tickers.value = updatedTickers
    }

    fun loadTickers() {
        viewModelScope.launch {
            _isLoading.value = true

            _tickers.value = repository.getCompanyInfo("cuobpm9r01qve8psc3f0cuobpm9r01qve8psc3fg")

            _isLoading.value = false
        }
    }

    fun searchTickers(q: String) {
        viewModelScope.launch {
            _isLoading.value = true

            _tickers.value = repository.searchCompany(
                q = q,
                exchange = "US",
                apiKey = "cuobpm9r01qve8psc3f0cuobpm9r01qve8psc3fg"
            )

            _isLoading.value = false
        }
    }

    fun setLoadingStatus(isSearchung: Boolean) {
        _isSearching.value = isSearchung
    }
}
