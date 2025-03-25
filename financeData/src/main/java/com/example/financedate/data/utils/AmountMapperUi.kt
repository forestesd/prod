package com.example.financedate.data.utils

import com.example.financedate.domain.models.AmountUi
import com.example.financedate.data.db.GoalEntity
import com.example.financedate.data.db.TransactionEntity
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun amountMapperUi(
    transactions: List<TransactionEntity>,
    goals: List<GoalEntity>
): AmountUi {
    val currentMonth = LocalDate.now().monthValue
    val currentYear = LocalDate.now().year
    val totalAmount = goals.sumOf { it.currentCollected }

    val deposit = transactions
        .filter { it.type == "Пополнение" }
        .filter { transaction ->
            val transactionDate = LocalDateTime.parse(
                transaction.date,
                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
            )
            transactionDate.monthValue == currentMonth && transactionDate.year == currentYear
        }
        .sumOf { it.amount }

    val removal = transactions
        .filter { it.type == "Снятие" }
        .filter { transaction ->
            val transactionDate = LocalDateTime.parse(
                transaction.date,
                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
            )
            transactionDate.monthValue == currentMonth && transactionDate.year == currentYear
        }
        .sumOf { it.amount }


    val depositMonth = deposit.minus(removal)
    val totalCostGoals = goals.sumOf { it.totalCoastTarget }

    val yearsLeft: BigDecimal = if (depositMonth > BigDecimal.ZERO) {
        val result = totalCostGoals.divide(depositMonth, 10, RoundingMode.HALF_UP)
        result.divide(BigDecimal(12), 2, RoundingMode.HALF_UP)
    } else {
        BigDecimal.ZERO
    }

    val monthsLeft = if (depositMonth > BigDecimal.ZERO) {
        totalCostGoals.divide(depositMonth, RoundingMode.HALF_UP).toInt()
    } else {
        0
    }

    val yearsText = when {
        totalCostGoals <= totalAmount -> "все цели достигнуты"

        yearsLeft < BigDecimal.ONE && yearsLeft > BigDecimal.ZERO -> {
            "$monthsLeft мес."
        }


        yearsLeft.toInt() == 1 -> "через 1 год"
        yearsLeft.toInt() in 2..4 -> "через $yearsLeft года"
        yearsLeft.toInt() in 5..Int.MAX_VALUE -> "через $yearsLeft лет"
        else -> "нет пополнений"
    }

    val goalComplete = if (totalCostGoals != BigDecimal.ZERO) totalAmount.divide(
        totalCostGoals,
        2,
        RoundingMode.HALF_UP
    )
        .multiply(BigDecimal(100)) else BigDecimal.ZERO

    return AmountUi(
        totalAmount = formatedBigDecimalWithSpaces(totalAmount),
        goalComplete = formatedBigDecimalWithSpaces(goalComplete),
        depositMonths = "${formatedBigDecimalWithSpaces(depositMonth)}/мес.",
        goalsComplete = yearsText
    )
}


fun formatedBigDecimalWithSpaces(value: BigDecimal): String {

    val symbols = DecimalFormatSymbols(Locale("ru", "RU")).apply {
        groupingSeparator = ' '
    }

    val format = DecimalFormat("#,##0.###", symbols)

    return format.format(value)
}