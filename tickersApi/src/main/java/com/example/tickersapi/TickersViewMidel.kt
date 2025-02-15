package com.example.tickersapi

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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

    fun loadTickers(){
        viewModelScope.launch {
            _isLoading.value = true

            _tickers.value = repository.getCompanyInfo("cuo2b81r01qokt74hgsgcuo2b81r01qokt74hgt0")

            _isLoading.value = false
        }
    }
}


class TickersViewModelFactory(private val repository: TickersRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TickersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TickersViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}