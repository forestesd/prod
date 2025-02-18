package com.example.financeui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financedate.FinaceViewModel

@Composable
fun FinanceMainScreen(financeViewModel: FinaceViewModel) {
    var showGoalsDialog by remember { mutableStateOf(false) }
    var showTransactionDialog by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        financeViewModel.getGoalProgress()
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Финансы",
            fontSize = 34.sp,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 10.dp, top = 16.dp)
        )
        HorizontalDivider(modifier = Modifier.padding(top = 10.dp))
        SavingsWiget(financeViewModel)
        GoalsFrame(financeViewModel)


        addButtons("Добавить цель") { showGoalsDialog = true}

        AddGoalOrTransactionDialog(
            financeViewModel.goalsWithProgress.value,
            dialogType = "goal",
            showGoalsDialog,
            onBack = { showGoalsDialog = false },
            onAddGoal = { name, amount, date ->
                financeViewModel.addGoal(name, amount, date)
                showGoalsDialog = false
            },
            addTransaction = { _, _, _, _ -> }
        )

        HorizontalDivider(Modifier.padding(top = 10.dp))
        addButtons("Добавить операцию") { showTransactionDialog = true}

        AddGoalOrTransactionDialog(
            financeViewModel.goalsWithProgress.value,
            dialogType = "transaction",
            showTransactionDialog,
            onBack = { showTransactionDialog = false },
            onAddGoal = { _, _, _ -> },
            addTransaction = {goalName, summ, typeOftransaction, comment ->
                financeViewModel.addTransaction(goalName, summ, typeOftransaction, comment)
                showTransactionDialog = false
            }
        )

    }
}

@Composable
fun addButtons(text: String, onClick: () -> Unit ){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(start = 10.dp),
            text = text
        )
        Spacer(modifier = Modifier.weight(1f))
        TextButton(
            onClick = {
               onClick()
            },
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(
                text = "+"
            )
        }
    }
}