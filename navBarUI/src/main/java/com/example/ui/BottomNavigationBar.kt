package com.example.ui

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

@Composable
fun BottomNavigationBar(
    selectedItem: Int,
    onMainClick: () -> Unit,
    onFinanceClick: () -> Unit,
    onNewsFeedClicked: () -> Unit
) {
    NavigationBar(
        modifier = Modifier.navigationBarsPadding(),
        containerColor = Color.White
    ) {
        NavigationBarItem(
            selected = selectedItem == 0,
            onClick = {
                onMainClick()
            },
            label = { Text("Главная") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") }
        )

        NavigationBarItem(
            selected = selectedItem == 1,
            onClick = {
                onFinanceClick()
            },
            label = { Text("Финансы") },
            icon = {
                Icon(
                    painterResource(R.drawable.account_balance_icon),
                    contentDescription = "Finance"
                )
            }
        )

        NavigationBarItem(
            selected = selectedItem == 2,
            onClick = {
                onNewsFeedClicked()
            },
            label = { Text("Лента") },
            icon = {
                Icon(
                    painterResource(R.drawable.news_feed_icon),
                    contentDescription = "NewsFeed"
                )
            }
        )
    }


}

