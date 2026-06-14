package com.example.todolist.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todolist.ui.components.TodoCard
import com.example.todolist.ui.components.TopBarDefault
import kotlinx.serialization.Serializable

@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: HomeViewModel = viewModel()
    val screenState = viewModel.uiState.collectAsState().value

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBarDefault("Lista de Tarefas")
        },
        containerColor = Color(0xFF131313)
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.padding(innerPadding)
        ) {
            items(
                items = screenState.tasks ?: emptyList(),
                key = { it.id }
            ) { task ->
                TodoCard(
                    task = task,
                    onCheckedChange = { viewModel.onCheckedChange(task.id, !task.completed) },
                )
            }
        }
    }
}

@Serializable
object Home