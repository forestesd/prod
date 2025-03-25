package com.example.financeui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financedate.data.FinanceViewModel

@Composable
fun SavingsWidget(financeViewModel: FinanceViewModel) {
    val amount by financeViewModel.allAmount
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.2f)
            .shadow(
                8.dp,
                RoundedCornerShape(
                    bottomStart = 16.dp,
                    bottomEnd = 16.dp,
                )
            )
            .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Всего: ${amount.totalAmount} ₽",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp)
            )
            RowsWithText("Достижение целей", "${amount.goalComplete}%")
            RowsWithText("Поступление",amount.depositMonths)
            RowsWithText("Когда", amount.goalsComplete)

        }
    }
}

@Composable
fun RowsWithText(text: String, data: String){
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp, horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = data,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}