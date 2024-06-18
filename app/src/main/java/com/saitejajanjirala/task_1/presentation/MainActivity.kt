package com.saitejajanjirala.task_1.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.saitejajanjirala.task_1.domain.models.Item
import com.saitejajanjirala.task_1.ui.theme.Task1Theme
import com.saitejajanjirala.task_1.domain.models.Result
import dagger.hilt.android.AndroidEntryPoint
import java.util.SortedMap

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Task1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ItemListScreen()
                }
            }
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ItemListScreen() {

        val itemsResult by viewModel.items.collectAsState()
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Item List") },
                    actions = {
                        IconButton(onClick = { viewModel.fetchItems() }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                        }
                    }
                )
            }
        ) {paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                when (val result = itemsResult) {
                    is Result.Loading -> LoadingView()
                    is Result.Success -> result.data?.let { ItemList(it) }
                    is Result.Error -> ErrorView(result.message)
                }
            }
        }
    }

    @Composable
    fun LoadingView() {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }

    @Composable
    fun ErrorView(message: String?) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error: $message", color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(8.dp))
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun ItemList(groupedItems: SortedMap<String, List<Item>>) {
        LazyColumn {
            groupedItems.forEach { (listId, items) ->
                stickyHeader {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primary) // Custom color for sticky header
                    ) {
                        Text(
                            text = "List ID: $listId",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(paddingValues = PaddingValues(horizontal = 8.dp)) // Padding for the text inside sticky header
                        )
                    }
                }
                items(items) { item ->
                    Column (
                        modifier = Modifier.fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f ))
                    ){
                        ItemCard(item)
                        Divider(color = Color.White, thickness = 1.dp) // Divider between cards
                    }
                }
            }
        }
    }


    @Composable
    fun ItemCard(item: Item) {
        Column(modifier = Modifier.padding(4.dp)) {
            Text(text = "ID: ${item.id}")
            Text(text = "Name: ${item.name}")
        }
    }
}

