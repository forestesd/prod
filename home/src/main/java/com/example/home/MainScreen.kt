package com.example.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.apis.NewsRepository
import com.example.apis.NewsViewModel
import com.example.apis.NewsViewModelFactory

@Composable
fun MainScreen(newsRepository: NewsRepository ) {
    val factory = NewsViewModelFactory(newsRepository)
    val viewModel: NewsViewModel = viewModel(factory = factory)


    LaunchedEffect(Unit) {
        viewModel.loadNews()
        Log.i("INFO", viewModel.news.toString())
    }

    val news by viewModel.news


    LazyColumn(
       modifier =  Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        contentPadding = PaddingValues(16.dp)
    ) {
        items(news.size) {item ->

            Card(
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                shape = RoundedCornerShape(12.dp)
            ) {


                Text(text = news[item].title)
            }

        }
    }
}