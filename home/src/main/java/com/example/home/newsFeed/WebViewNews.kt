package com.example.home.newsFeed

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import com.example.apis.data.NewsViewModel
import com.example.tickersapi.data.TickersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewNews(
    newsUrl: String,
    newsViewModel: NewsViewModel,
    tickersViewModel: TickersViewModel,
    onBack: () -> Unit,
    onShare: () -> Unit
) {
    LaunchedEffect(Unit) {
        newsViewModel.setIsSearching(false)
        tickersViewModel.setLoadingStatus(false)
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = { Text(text = "Новости") },
            navigationIcon = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = { onBack() }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Стрелка назад")
                    }
                    IconButton(
                        onClick = { onShare() }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Поделиться")
                    }
                }

            },
            colors = TopAppBarDefaults.topAppBarColors(Color.White)
        )



        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    webViewClient = WebViewClient()
                    loadUrl(newsUrl)
                }
            },
            modifier = Modifier.fillMaxSize(),
        )

    }


}