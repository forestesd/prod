package com.example.financedate.data

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.financedate.data.db.GoalEntity
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import com.example.financedate.domain.models.AmountUi
import com.example.financedate.domain.models.GoalWithProgress
import com.example.financedate.domain.models.TransactionUi
import com.example.financedate.domain.use_cases.AddGoalUseCase
import com.example.financedate.domain.use_cases.AddTransactionUseCase
import com.example.financedate.domain.use_cases.AllAmountUseCase
import com.example.financedate.domain.use_cases.DeleteGoalUseCase
import com.example.financedate.domain.use_cases.GetGoalProgressUseCase
import com.example.financedate.domain.use_cases.GetTransactionsUseCase
import java.math.BigDecimal

class FinanceViewModel @Inject constructor(
    private val addGoalUseCase: AddGoalUseCase,
    private val addTransactionUseCase: AddTransactionUseCase,
    private val allAmountUseCase: AllAmountUseCase,
    private val deleteGoalUseCase: DeleteGoalUseCase,
    private val getGoalProgressUseCase: GetGoalProgressUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase
) : ViewModel() {

    private val _allTransactions = mutableStateOf<List<TransactionUi>>(emptyList())
    val allTransaction: State<List<TransactionUi>> = _allTransactions

    private val _goalsWithProgress = mutableStateOf<List<GoalWithProgress>>(emptyList())
    val goalsWithProgress: State<List<GoalWithProgress>> = _goalsWithProgress

    private val _allAmount = mutableStateOf(AmountUi("", "", "", ""))
    val allAmount: State<AmountUi> = _allAmount

    fun addGoal(name: String, targetCost: BigDecimal, deadLine: String?) {
        viewModelScope.launch {
            addGoalUseCase.invoke(
                name = name,
                targetCost = targetCost,
                deadLine = deadLine
            )
            getGoalProgress()
            allAmount()
        }

    }


    fun getGoalProgress() {
        viewModelScope.launch {
            _goalsWithProgress.value = getGoalProgressUseCase.invoke()
        }
    }

    fun deleteGoal(goal: GoalEntity) {
        viewModelScope.launch {
            deleteGoalUseCase.invoke(goal)
            getGoalProgress()
        }
    }

    fun addTransaction(goalName: String, amount: BigDecimal, type: String, comment: String?) {
        viewModelScope.launch {
            addTransactionUseCase.invoke(
                goalName = goalName,
                amount = amount,
                type = type,
                comment = comment
            )
        }
    }

    fun getTransactions() {
        viewModelScope.launch {
            _allTransactions.value = getTransactionsUseCase.invoke()
            getGoalProgress()
            getTransactions()
            allAmount()
        }
    }

    fun allAmount() {
        viewModelScope.launch {
            _allAmount.value = allAmountUseCase.invoke()
        }
    }

}
