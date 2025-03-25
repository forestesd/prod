package com.example.financedate.data.repository

import androidx.compose.ui.graphics.Color
import com.example.financedate.domain.models.AmountUi
import com.example.financedate.domain.models.GoalWithProgress
import com.example.financedate.domain.models.TransactionUi
import com.example.financedate.data.db.GoalDAO
import com.example.financedate.data.db.GoalEntity
import com.example.financedate.data.db.TransactionDao
import com.example.financedate.data.db.TransactionEntity
import com.example.financedate.data.utils.amountMapperUi
import com.example.financedate.data.utils.transactionMapperToUi
import com.example.financedate.domain.repository.FinanceRepositoryInterface
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class FinanceRepository @Inject constructor(
    private val transactionDao: TransactionDao,
    private val goalDAO: GoalDAO,
) : FinanceRepositoryInterface {
    override suspend fun addGoal(name: String, targetCost: BigDecimal, deadLine: String?) {
        val goal = GoalEntity(name = name, totalCoastTarget = targetCost, deadLine = deadLine)
        goalDAO.insert(goal)

    }

    override suspend fun getGoalProgress(): List<GoalWithProgress> {
        val allGoals = goalDAO.getAllGoals()

        return allGoals.map { goal ->
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
    }

    override suspend fun deleteGoal(goal: GoalEntity) {
        if (goal.currentCollected.toInt() > 0) {
            addTransaction(goal.name, goal.currentCollected, type = "Снятие", comment = null)
            goalDAO.delete(goal)
        } else {
            goalDAO.delete(goal)

        }

    }

    override suspend fun addTransaction(
        goalName: String,
        amount: BigDecimal,
        type: String,
        comment: String?
    ) {
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

    }

    override suspend fun getTransactions(): List<TransactionUi> {
        val transactions = transactionDao.getAllTransactions()
        return transactions.map { transaction ->
            transactionMapperToUi(transaction.goalName, transaction)
        }
    }

    override suspend fun allAmount(): AmountUi {
        val goals = goalDAO.getAllGoals()
        val transactions = transactionDao.getAllTransactions()
        return amountMapperUi(transactions, goals)
    }
}