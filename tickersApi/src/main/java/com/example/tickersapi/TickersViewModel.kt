package com.example.tickersapi

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TickersViewModel @Inject constructor(
    private val repository: TickersRepository
): ViewModel() {
    private val _tickers = mutableStateOf<List<TickerUi>>(emptyList())
    val tickers: State<List<TickerUi>> = _tickers

    private var _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    fun loadTickers(){
        viewModelScope.launch {
            _isLoading.value = true

            _tickers.value = repository.getCompanyInfo("cuobpm9r01qve8psc3f0cuobpm9r01qve8psc3fg")

            _isLoading.value = false
        }
    }
}
