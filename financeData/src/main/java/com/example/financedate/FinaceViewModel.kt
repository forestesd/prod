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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FinaceViewModel @Inject constructor(
    application: Application,
    private val goalDAO: GoalDAO,
    private val transactionDao: TransactionDao
) : AndroidViewModel(application) {

    private val _allTransactions = mutableStateOf<List<TransactionUi>>(emptyList())
    val allTransaction: State<List<TransactionUi>> = _allTransactions

    private val _goalsWithProgress = mutableStateOf<List<GoalWithProgress>>(emptyList())
    val goalsWithProgress: State<List<GoalWithProgress>> = _goalsWithProgress

    private val _allAmmount = mutableStateOf<Float>(0f)
    val allAmmount: State<Float> = _allAmmount

    fun addGoal(name: String, targetCost: Double, deadLine: String?) {
        viewModelScope.launch {
            val goal = GoalEntity(name = name, totalCoastTarget = targetCost, deadLine = deadLine)
            goalDAO.insert(goal)
            getGoalProgress()
        }
    }


    fun getGoalProgress() {
        viewModelScope.launch {
            val allGoals = goalDAO.getAllGoals()
            val goalsWithProgress = allGoals.map { goal ->
                val progress =
                    (goal.currentCollected / goal.totalCoastTarget).coerceIn(0.0, 1.0)

                val colorProgress = when {
                    progress > 0.75 -> Color.Green
                    progress > 0.4 -> Color.Yellow
                    else -> Color.Red
                }

                GoalWithProgress(goal, progress.toFloat(), colorProgress)
            }
            _goalsWithProgress.value = goalsWithProgress
        }
    }

    fun deleteGoal(goal: GoalEntity) {
        viewModelScope.launch {
            if (goal.currentCollected > 0) {
                addTransaction(goal.name, goal.currentCollected, type = "Снятие", comment = null)
                goalDAO.delete(goal)
            } else {
                goalDAO.delete(goal)
                getGoalProgress()
            }
        }
    }

    fun addTransaction(goalName: String, ammount: Double, type: String, comment: String?) {
        viewModelScope.launch {
            val goalByName = goalDAO.getGoalByName(goalName)

            val transaction = TransactionEntity(
                goalName = goalByName.name,
                amount = ammount,
                type = type,
                comment = comment,
                date = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))
            )
            transactionDao.insert(transaction)


            val newAmmount =
                if (type == "Пополнение") goalByName.currentCollected + ammount else goalByName.currentCollected - ammount
            goalDAO.update(goalByName.copy(currentCollected = newAmmount))
            getGoalProgress()
            getTransactions()
            allAmmount()
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

    fun allAmmount() {
        viewModelScope.launch {

            val goals = goalDAO.getAllGoals()
            _allAmmount.value = goals.sumOf { it.currentCollected }.toFloat()
        }
    }

    fun checkSummCorrect(summ: Float, goalSumm: Float): Boolean {
        return if (goalSumm - summ < 0) false else true
    }
}
