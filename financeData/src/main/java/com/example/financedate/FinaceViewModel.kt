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
        }
    }

    private fun getAllGoals() {
        viewModelScope.launch {
            _allGoals.value = goalDAO.getAllGoals()
        }
    }

    fun getGoalProgress() {
        viewModelScope.launch {
            getAllGoals()
            val goalsWithProgress = _allGoals.value.map{ goal ->
               val progress = ((goal.currentCollected +10000)/ goal.totalCoastTarget).coerceIn(0.0, 1.0)
                GoalWithProgress(goal, progress.toFloat())
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

    fun addTransaction(goalId: Int, ammount: Double, type: String, comment: String?) {
        viewModelScope.launch {
            val transaction = TransactionEntity(
                idGoal = goalId,
                amount = ammount,
                type = type,
                comment = comment,
                date = "2025-02-17"
            )
            transactionDao.insert(transaction)


            val goal = goalDAO.getGoalById(goalId)
            if (goal != null) {
                val newAmmount =
                    if (type == "deposit") goal.currentCollected + ammount else goal.currentCollected - ammount
                goalDAO.update(goal.copy(currentCollected = newAmmount))
            }
        }
    }


}