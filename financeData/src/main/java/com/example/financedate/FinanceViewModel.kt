package com.example.financedate

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.financedate.db.GoalEntity
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import java.math.BigDecimal

class FinanceViewModel @Inject constructor(
    private val financeRepository: FinanceRepository
) : ViewModel() {

    private val _allTransactions = mutableStateOf<List<TransactionUi>>(emptyList())
    val allTransaction: State<List<TransactionUi>> = _allTransactions

    private val _goalsWithProgress = mutableStateOf<List<GoalWithProgress>>(emptyList())
    val goalsWithProgress: State<List<GoalWithProgress>> = _goalsWithProgress

    private val _allAmount = mutableStateOf(AmountUi("", "", "", ""))
    val allAmount: State<AmountUi> = _allAmount

    fun addGoal(name: String, targetCost: BigDecimal, deadLine: String?) {
        viewModelScope.launch {
            financeRepository.addGoal(name, targetCost, deadLine)
            getGoalProgress()
            allAmount()
        }

    }


    fun getGoalProgress() {
        viewModelScope.launch {
            _goalsWithProgress.value = financeRepository.getGoalProgress()
        }
    }

    fun deleteGoal(goal: GoalEntity) {
        viewModelScope.launch {
            financeRepository.deleteGoal(goal)
            getGoalProgress()
        }
    }

    fun addTransaction(goalName: String, amount: BigDecimal, type: String, comment: String?) {
        viewModelScope.launch {
            financeRepository.addTransaction(
                goalName = goalName,
                amount = amount,
                type = type,
                comment = comment
            )
        }
    }

    fun getTransactions() {
        viewModelScope.launch {
            _allTransactions.value = financeRepository.getTransactions()
            getGoalProgress()
            getTransactions()
            allAmount()
        }
    }

    fun allAmount() {
        viewModelScope.launch {
            _allAmount.value = financeRepository.allAmount()
        }
    }

}
