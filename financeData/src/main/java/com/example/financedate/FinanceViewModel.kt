package com.example.financedate

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.financedate.db.GoalDAO
import com.example.financedate.db.GoalEntity
import com.example.financedate.db.TransactionDao
import com.example.financedate.db.TransactionEntity
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FinanceViewModel @Inject constructor(
    application: Application,
    private val goalDAO: GoalDAO,
    private val transactionDao: TransactionDao
) : AndroidViewModel(application) {

    private val _allTransactions = mutableStateOf<List<TransactionUi>>(emptyList())
    val allTransaction: State<List<TransactionUi>> = _allTransactions

    private val _goalsWithProgress = mutableStateOf<List<GoalWithProgress>>(emptyList())
    val goalsWithProgress: State<List<GoalWithProgress>> = _goalsWithProgress

    private val _allAmount = mutableStateOf(AmountUi("", "", "", ""))
    val allAmount: State<AmountUi> = _allAmount

    fun addGoal(name: String, targetCost: BigDecimal, deadLine: String?) {
        viewModelScope.launch {
            val goal = GoalEntity(name = name, totalCoastTarget = targetCost, deadLine = deadLine)
            goalDAO.insert(goal)
            getGoalProgress()
            allAmount()
        }
    }


    fun getGoalProgress() {
        viewModelScope.launch {
            val allGoals = goalDAO.getAllGoals()
            val goalsWithProgress = allGoals.map { goal ->
                val progress = goal.currentCollected
                    .divide(goal.totalCoastTarget, 2, RoundingMode.HALF_UP)
                    .coerceIn(BigDecimal.ZERO, BigDecimal.ONE)

                val colorProgress = when {
                    progress > BigDecimal("0.75") -> Color.Green
                    progress > BigDecimal("0.4") -> Color.Yellow
                    else -> Color.Red
                }

                GoalWithProgress(goal, progress.toFloat(), colorProgress)
            }
            _goalsWithProgress.value = goalsWithProgress
        }
    }

    fun deleteGoal(goal: GoalEntity) {
        viewModelScope.launch {
            if (goal.currentCollected.toInt() > 0) {
                addTransaction(goal.name, goal.currentCollected, type = "Снятие", comment = null)
                goalDAO.delete(goal)
            } else {
                goalDAO.delete(goal)
                getGoalProgress()
            }
        }
    }

    fun addTransaction(goalName: String, amount: BigDecimal, type: String, comment: String?) {
        viewModelScope.launch {
            val goalByName = goalDAO.getGoalByName(goalName)

            val transaction = TransactionEntity(
                goalName = goalByName.name,
                amount = amount,
                type = type,
                comment = comment,
                date = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))
            )
            transactionDao.insert(transaction)


            val newAmount =
                if (type == "Пополнение") goalByName.currentCollected + amount else goalByName.currentCollected - amount
            goalDAO.update(goalByName.copy(currentCollected = newAmount))
            getGoalProgress()
            getTransactions()
            allAmount()
        }
    }

    fun getTransactions() {
        viewModelScope.launch {
            val transactions = transactionDao.getAllTransactions()
            val transactionUi = transactions.map { transaction ->
                transactionMapperToUi(transaction.goalName, transaction)
            }
            _allTransactions.value = transactionUi
        }
    }

    fun allAmount() {
        viewModelScope.launch {

            val goals = goalDAO.getAllGoals()
            val transactions = transactionDao.getAllTransactions()
            _allAmount.value = amountMapperUi(transactions, goals)
        }
    }

}
