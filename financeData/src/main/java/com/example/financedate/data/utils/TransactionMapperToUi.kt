package com.example.financedate.data.utils

import com.example.financedate.domain.models.TransactionUi
import com.example.financedate.data.db.TransactionEntity

fun transactionMapperToUi(goalName: String, transaction: TransactionEntity): TransactionUi {
    return TransactionUi(
        id = transaction.id,
        transactionType = transaction.type,
        amount = if (transaction.type == "Пополнение") "+ ${formatedBigDecimalWithSpaces(transaction.amount)} ₽" else "- ${
            formatedBigDecimalWithSpaces(
                transaction.amount
            )
        } ₽",
        goalName = goalName,
        comments = transaction.comment
    )
}