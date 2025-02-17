package com.example.tickersapi

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class TickersViewModel @Inject constructor(
    private val repository: TickersRepository
): ViewModel() {
    private val _tickers = mutableStateOf<List<TickerUi>>(emptyList())
    val tickers: State<List<TickerUi>> = _tickers

    private var _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private var _isSearching = mutableStateOf(false)
    val isSearching: State<Boolean> = _isSearching

    fun loadTickers(){
        viewModelScope.launch {
            _isLoading.value = true

            _tickers.value = repository.getCompanyInfo("cuobpm9r01qve8psc3f0cuobpm9r01qve8psc3fg")

            _isLoading.value = false
        }
    }

    fun searchTickers(q: String){
        viewModelScope.launch {
            _isLoading.value = true

            _tickers.value = repository.searchCompany(q = q, exchange = "US", apiKey = "cuobpm9r01qve8psc3f0cuobpm9r01qve8psc3fg")

            _isLoading.value = false
        }
    }

    fun setLoadingStatus(isSearchung: Boolean){
        _isSearching.value = isSearchung
    }
}
