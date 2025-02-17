package com.example.financeui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.financedate.FinaceViewModel

@Composable
fun SavingsWiget(finaceViewModel: FinaceViewModel) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
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

    }
}