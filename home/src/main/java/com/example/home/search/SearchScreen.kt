package com.example.home.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apis.data.NewsViewModel
import com.example.tickersapi.TickersViewModel
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(newsViewModel: NewsViewModel, tickersViewModel: TickersViewModel) {
    var query by remember { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    val isSearching by newsViewModel.isSearching.collectAsState()
    if (!isSearching){
        Text(
            text = "Главная",
            modifier = Modifier.padding(start = 10.dp, top = 16.dp),
            fontSize = 34.sp
        )
    }

    Box(Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .semantics { isTraversalGroup = true }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            DockedSearchBar(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = SearchBarDefaults.colors(
                    containerColor = Color.LightGray,
                ),
                inputField = {
                    SearchBarDefaults.InputField(
                        query = query,
                        onQueryChange = {
                            query = it

                        },
                        onSearch = { },
                        expanded = expanded,
                        onExpandedChange = {
                            expanded = it
                            newsViewModel.setIsSearching(expanded)
                            tickersViewModel.setLoadingStatus(expanded)
                        },
                        placeholder = { Text("Поиск") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = { Icon(Icons.Default.MoreVert, contentDescription = null) }
                    )
                },
                expanded = false,
                onExpandedChange = { expanded = it }
            ) {

            }

            LaunchedEffect(query) {
                delay(500)
                if (query.isNotEmpty()) {
                    newsViewModel.loadSearchNews(query)
                    tickersViewModel.searchTickers(query)
                }

            }
            if (expanded) {
                TextButton(
                    onClick = {
                        query = ""
                        expanded = false
                        newsViewModel.setIsSearching(expanded)
                        tickersViewModel.setLoadingStatus(expanded)
                        tickersViewModel.loadTickers()
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Отменить")
                }
            }
        }
    }
}