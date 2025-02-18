package com.example.financeui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financedate.FinanceViewModel

@Composable
fun FinanceMainScreen(financeViewModel: FinanceViewModel) {
    var showGoalsDialog by remember { mutableStateOf(false) }
    var showTransactionDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        financeViewModel.getGoalProgress()
        financeViewModel.getTransactions()
        financeViewModel.allAmount()
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
        SavingsWidget(financeViewModel)
        GoalsFrame(financeViewModel)


        AddButtons(text = "Добавить цель", enabled = true, onClick = { showGoalsDialog = true })

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

        HorizontalDivider(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 10.dp)
                .height(10.dp)
                .clip(RoundedCornerShape(50.dp)),
            thickness = 10.dp,
            color = Color.LightGray
        )


        AddGoalOrTransactionDialog(
            financeViewModel.goalsWithProgress.value,
            dialogType = "transaction",
            showTransactionDialog,
            onBack = { showTransactionDialog = false },
            onAddGoal = { _, _, _ -> },
            addTransaction = { goalName, sum, typeTransaction, comment ->
                financeViewModel.addTransaction(goalName, sum, typeTransaction, comment)
                showTransactionDialog = false
            }
        )

        TransactionFrame(financeViewModel, onClickAddButton = { showTransactionDialog = true })

    }
}

@Composable
fun AddButtons(text: String, onClick: () -> Unit, enabled: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Spacer(modifier = Modifier.weight(1f))
        TextButton(
            enabled = enabled,
            onClick = {
                onClick()
            },
            modifier = Modifier.align(Alignment.CenterVertically),
            colors = ButtonColors(
                contentColor = Color.Black,
                containerColor = Color.White,
                disabledContentColor = Color.LightGray,
                disabledContainerColor = Color.White
            )
        ) {
            Text(
                modifier = Modifier.padding(start = 10.dp, end = 15.dp),
                text = text,
                fontWeight = FontWeight.Bold
            )
            if (text == "Добавить цель") {
                Spacer(modifier = Modifier.weight(1f))
            }
            Text(
                text = "+",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}