package com.example.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Newsfeed() {
    Column {
        Button(
            modifier = Modifier
                .height(50.dp)
                .width(85.dp),
            onClick = {}
        ) {
            Text("Куда-то")
        }
    }
}