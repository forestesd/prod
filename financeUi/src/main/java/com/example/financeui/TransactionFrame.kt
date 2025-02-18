package com.example.financeui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financedate.FinaceViewModel

@Composable
fun TransactionFrame(financeViewModel: FinaceViewModel, onClickAddButton: () -> Unit) {
    val transactions by financeViewModel.allTransaction
    AddButtons(
        "Добавить операцию",
        enabled = financeViewModel.goalsWithProgress.value.isNotEmpty(),
        onClick = onClickAddButton
    )
    if (transactions.isEmpty()) {
        Text(
            text = "Нет транзакций",
            fontSize = 32.sp
        )
    }

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        items(transactions, key = { item -> item.id }) { item ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(bottom = 10.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.transactionType,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = item.ammount,
                        fontSize = 16.sp
                    )
                }
                Text(
                    text = "На -> ${item.goalName}",
                    fontSize = 16.sp
                )
                if (item.comments != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                            .padding(horizontal = 10.dp)
                    ) {
                        Text(
                            text = item.comments!!,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                }
            }
        }
    }
}