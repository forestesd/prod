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

class FinaceViewModel @Inject constructor(
    application: Application,
    private val goalDAO: GoalDAO,
    private val transactionDao: TransactionDao
) : AndroidViewModel(application) {

    private val _allGoals = mutableStateOf<List<GoalEntity>>(emptyList())
    val allGoals: State<List<GoalEntity>> = _allGoals

    private val _goalsWithProgress = mutableStateOf<List<GoalWithProgress>>(emptyList())
    val goalsWithProgress: State<List<GoalWithProgress>> = _goalsWithProgress

    fun addGoal(name: String, targetCost: Double, deadLine: String?) {
        viewModelScope.launch {
            val goal = GoalEntity(name = name, totalCoastTarget = targetCost, deadLine = deadLine)
            goalDAO.insert(goal)
            getGoalProgress()
        }
    }

    private fun getAllGoals() {
        viewModelScope.launch {
            _allGoals.value = goalDAO.getAllGoals()
        }
    }

    fun getGoalProgress() {
        viewModelScope.launch {
            val allGoals = goalDAO.getAllGoals()
            val goalsWithProgress = allGoals.map { goal ->
                val progress =
                    (goal.currentCollected  / goal.totalCoastTarget).coerceIn(0.0, 1.0)

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
            transactionDao.deleteTransactionsForGoal(goal.id)
            goalDAO.delete(goal)
        }
    }

    fun addTransaction(goalName: String, ammount: Double, type: String, comment: String?) {
        viewModelScope.launch {
            val goalByName = goalDAO.getGoalByName(goalName)

            val transaction = TransactionEntity(
                idGoal = goalByName.id,
                amount = ammount,
                type = type,
                comment = comment,
                date = "2025-02-17"
            )
            transactionDao.insert(transaction)


            val newAmmount =
                if (type == "Пополнение") goalByName.currentCollected + ammount else goalByName.currentCollected - ammount
            goalDAO.update(goalByName.copy(currentCollected = newAmmount))
            getGoalProgress()
        }
    }
}
