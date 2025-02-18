package com.example.financedate

import com.example.financedate.db.TransactionEntity

fun transactionMapperToUi(goalName: String, transaction: TransactionEntity): TransactionUi {
    return TransactionUi(
        id = transaction.id,
        transactionType = transaction.type,
        ammount = if (transaction.type == "Пополнение") "+ ${transaction.amount}₽" else "- ${transaction.amount}₽",
        goalName = goalName,
        comments = transaction.comment
    )
}