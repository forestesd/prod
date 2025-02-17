package com.example.financeui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGoalDialog(
    showDialog: Boolean,
    onBack: () -> Unit,
    onAddGoal: (String, Double, String?) -> Unit
) {
    if (showDialog) {
        var goalName by remember { mutableStateOf("") }
        var goalTotalCost by remember { mutableStateOf("") }
        var goalDate by remember { mutableStateOf("") }

        Dialog(
            onDismissRequest = { onBack() },
            ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text("Новая цель", style = MaterialTheme.typography.titleLarge)

                    OutlinedTextField(
                        value = goalName,
                        onValueChange = { goalName = it },
                        label = { Text("Название цели") }
                    )

                    OutlinedTextField(
                        value = goalTotalCost,
                        onValueChange = { goalTotalCost = it },
                        label = { Text("Требуемая сумма") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = goalDate,
                        onValueChange = { goalDate = it },
                        label = { Text("Дата (необязательно)") }
                    )
                    PositiveAndNegativeButton(onBack, onAddGoal, goalTotalCost, goalName, goalDate)
                }
            }

        }
    }
}

@Composable

fun PositiveAndNegativeButton(
    onBack: () -> Unit,
    onAddGoal: (String, Double, String?) -> Unit,
    goalTotalCost: String,
    goalName: String,
    goalDate: String?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        TextButton(onClick = { onBack() }) {
            Text("Отмена")
        }
        Spacer(modifier = Modifier.width(8.dp))
        TextButton(
            onClick = {
                val amount = goalTotalCost.toDoubleOrNull() ?: 0.0
                onAddGoal(goalName, amount, goalDate?.ifEmpty { null })
                onBack()
            }
        ) {
            Text("Сохранить")
        }
    }
}