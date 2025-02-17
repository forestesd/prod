package com.example.financedate

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.financedate.db.FinanceDB
import com.example.financedate.db.GoalEntity
import com.example.financedate.db.TransactionEntity
import kotlinx.coroutines.launch

class FinaceViewModel(application: Application) : AndroidViewModel(application) {
    private val financeDb = FinanceDB.getDB(application)
    private val goalDAO = financeDb.goalDao()
    private val transactionDao = financeDb.transactionDao()

    fun addGoal(name: String, targetCost: Double, deadLine: String?) {
        viewModelScope.launch {
            val goal = GoalEntity(name = name, totalCoastTarget = targetCost, deadLine = deadLine)
            goalDAO.insert(goal)
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
            if (goal != null){
                val newAmmount = if(type == "deposit") goal.currentCollected + ammount else goal.currentCollected-ammount
                goalDAO.update(goal.copy(currentCollected = newAmmount))
            }
        }
    }


}