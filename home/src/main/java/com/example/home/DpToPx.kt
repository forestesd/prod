package com.example.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PxToDp(px: Float): Dp {
    val density = LocalDensity.current.density // Получаем плотность экрана
    return (px / density).dp // Переводим px в dp
}