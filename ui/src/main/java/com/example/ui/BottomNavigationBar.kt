package com.example.ui


import android.graphics.Color.alpha
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavigationBar(onNavigate: (String) -> Unit) {
    var selectedItem by remember { mutableIntStateOf(0) }
    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            BottomNavigation(
                modifier = Modifier.navigationBarsPadding(),
                backgroundColor = Color.White.copy(alpha = 0.5f),
                elevation = 0.dp
            ) {
                BottomNavigationItem(
                    selected = selectedItem == 0,
                    onClick = {
                        selectedItem = 0
                        onNavigate("main")
                    },
                    label = { Text("Главная") },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") }
                )

                BottomNavigationItem(
                    selected = selectedItem == 1,
                    onClick = {
                        selectedItem = 1
                        onNavigate("finance")
                    },
                    label = { Text("Финансы") },
                    icon = { Icon(painterResource(R.drawable.account_balance_icon), contentDescription = "dasdasd") }
                )

                BottomNavigationItem(
                    selected = selectedItem == 2,
                    onClick = {
                        selectedItem = 2
                        onNavigate("newsFeed")
                    },
                    label = { Text("Лента") },
                    icon = { Icon(painterResource(R.drawable.news_feed_icon), contentDescription = "dasdasd") }
                )
            }

        },
        content = {

        }
    )

}

