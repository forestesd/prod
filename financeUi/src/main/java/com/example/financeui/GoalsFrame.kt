package com.example.financeui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.window.Dialog
import com.example.financedate.data.FinanceViewModel
import com.example.financedate.domain.models.GoalWithProgress
import com.example.financedate.data.utils.formatedBigDecimalWithSpaces

@Composable
fun GoalObj(item: GoalWithProgress, financeViewModel: FinanceViewModel){
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        onClick = {
            showDeleteDialog = true
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.goal.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 10.dp, top = 5.dp)
            )
            if (item.goal.deadLine != null){
                Text(
                    text = "До ${item.goal.deadLine}",
                    modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                )
            }
            Spacer(Modifier.weight(1f))
            Text(
                text = "${formatedBigDecimalWithSpaces(item.goal.currentCollected)}/${
                    formatedBigDecimalWithSpaces(
                        item.goal.totalCoastTarget
                    )
                }",
                modifier = Modifier.padding(end = 10.dp, top = 5.dp)
            )
        }


        Row(
            modifier = Modifier
                .padding(10.dp)
                .height(15.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(color = Color.LightGray)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(item.progress)
                    .background(item.progressColor)
            )
        }
    }
    if (showDeleteDialog) {
        DeleteDialog(
            onDelete = {
                financeViewModel.deleteGoal(item.goal)
                showDeleteDialog = false
            },
            onBack = {
                showDeleteDialog = false
            },
            item.goal.name
        )
    }

}

@Composable
fun DeleteDialog(onDelete: () -> Unit, onBack: () -> Unit, goal: String) {
    Dialog(
        onDismissRequest = { onBack() },
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Вы действительно хотите удалить цель: $goal?")
                PositiveAndNegativeButton("Удалить", onBack, onDelete)
            }
        }
    }
}