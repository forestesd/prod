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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

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
import com.example.financedate.FinaceViewModel
import com.example.financedate.GoalWithProgress

@Composable
fun AddGoalOrTransactionDialog(
    goals: List<GoalWithProgress>,
    dialogType: String,
    showDialog: Boolean,
    onBack: () -> Unit,
    onAddGoal: (String, Double, String?) -> Unit,
    addTransaction: (String, Double, String, String?) -> Unit
) {
    if (showDialog) {

        var goalName by remember { mutableStateOf("") }
        var summ by remember { mutableStateOf("") }
        var dateOrComment by remember { mutableStateOf("") }
        var typeOfTransaction by remember { mutableStateOf("") }

        var correctSumm by remember { mutableStateOf(true) }
        var isGoalNameValid by remember { mutableStateOf(true) }
        var isSummValid by remember { mutableStateOf(true) }
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

                    Text(
                        text = if (dialogType == "goal") "Новая цель" else "Новая операция",
                        style = MaterialTheme.typography.titleLarge
                    )

                    if (dialogType == "transaction") {
                        if (!isGoalNameValid) {
                            Text(
                                "Название цели обязательно",
                                color = Color.Red,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        GoalDropDown(goalName, "Название цели", goals.map { it.goal.name }) {
                            goalName = it
                            isGoalNameValid = it.isNotBlank()
                        }
                    } else {
                        if (!isGoalNameValid) {
                            Text(
                                "Название цели обязательно",
                                color = Color.Red,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        OutlinedTextField(
                            value = goalName,
                            onValueChange = {
                                goalName = it
                                isGoalNameValid = it.isNotBlank()
                            },
                            label = { Text(text = "Название цели") }
                        )
                    }

                    if (dialogType == "transaction") {
                        GoalDropDown(
                            typeOfTransaction,
                            "Тип операции",
                            listOf("Пополнение", "Снятие")
                        ) {
                            typeOfTransaction = it
                        }
                    }
                    if (!isSummValid) {
                        Text(
                            "Введите корректную сумму",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    if (!correctSumm) {
                        Text(
                            "Недостаточно средств для снятия",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    OutlinedTextField(
                        value = summ,
                        onValueChange = {
                            summ = it
                            val enteredSum = it.toDoubleOrNull() ?: 0.0
                            val goal = goals.find { g -> g.goal.name == goalName }
                            correctSumm = when {
                                typeOfTransaction == "Снятие" && goal != null -> enteredSum <= goal.goal.currentCollected
                                else -> true
                            }

                        },
                        label = { Text("сумма") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = dateOrComment,
                        onValueChange = { dateOrComment = it },
                        label = {
                            Text(text = if (dialogType == "goal") "Дата (необязательно)" else "Комментарий (необязательно)")
                        }
                    )
                    PositiveAndNegativeButton(
                        text = "Сохранить",
                        onBack,
                        onSave = {
                            val enteredSum = summ.toDoubleOrNull() ?: 0.0

                            isGoalNameValid = goalName.isNotBlank()
                            isSummValid = summ.isNotBlank() && true && enteredSum > 0

                            if (isGoalNameValid && isSummValid) {
                                if (dialogType == "goal") {
                                    onAddGoal(
                                        goalName,
                                        summ.toDouble(),
                                        dateOrComment.takeIf { it.isNotEmpty() })
                                } else if (correctSumm) {
                                    addTransaction(
                                        goalName,
                                        summ.toDouble(),
                                        typeOfTransaction,
                                        dateOrComment.takeIf { it.isNotEmpty() }
                                    )
                                }
                            }
                        }
                    )
                }
            }

        }
    }
}

@Composable
fun PositiveAndNegativeButton(
    text: String,
    onBack: () -> Unit,
    onSave: () -> Unit,
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
                onSave()
            }
        ) {
            Text(text)
        }
    }
}

@Composable
fun GoalDropDown(
    selectedGoalOrTransaction: String?,
    text: String,
    goals: List<String>,
    onGoalSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(selectedGoalOrTransaction ?: "") }

    Column {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(text) },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Выбор цели"
                    )
                }
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            goals.forEach { goal ->
                DropdownMenuItem(
                    text = { Text(goal) },
                    onClick = {
                        selected = goal
                        onGoalSelected(goal)
                        expanded = false
                    }
                )
            }
        }
    }
}