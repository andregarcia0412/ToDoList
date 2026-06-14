package com.example.todolist.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todolist.ui.components.FloatingButton
import com.example.todolist.ui.components.GenericButton
import com.example.todolist.ui.components.TodoCard
import com.example.todolist.ui.components.TopBarDefault
import com.example.todolist.ui.screens.login.Login
import com.example.todolist.ui.screens.newtask.NewTask
import kotlinx.serialization.Serializable

@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: HomeViewModel = viewModel()
    val screenState = viewModel.uiState.collectAsState().value

    when {
        screenState.generalError != null -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF131313)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 32.dp)
                ) {
                    Text(
                        text = "Não foi possível carregar as tarefas.",
                        color = Color(0xFF9E9E9E),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    Box(modifier = Modifier.padding(top = 16.dp)) {
                        GenericButton(
                            text = "Voltar",
                            onClick = { navController.popBackStack() },
                            containerColor = Color(0xFF3A91FF)
                        )
                    }
                }
            }
        }

        screenState.isLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF131313)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF3A91FF))
            }
        }

        else -> {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopBarDefault(
                        title = "Lista de Tarefas",
                        icon = Icons.AutoMirrored.Filled.Logout,
                        onIconClick = {
                            viewModel.signOut()
                            navController.navigate(Login) {
                                popUpTo<Login> { inclusive = true }
                            }
                        }
                    )
                },
                containerColor = Color(0xFF131313),
                floatingActionButton = {
                    FloatingButton(
                        Icons.Default.Add,
                        onClick = { navController.navigate(NewTask()) }
                    )
                }
            ) { innerPadding ->
                if (screenState.tasks.isNullOrEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Você ainda não tem tarefas.\nToque no botão + para adicionar.",
                            color = Color(0xFF9E9E9E),
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(16.dp),
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        items(
                            items = screenState.tasks,
                            key = { it.id }
                        ) { task ->
                            TodoCard(
                                task = task,
                                onCheckedChange = { viewModel.onCheckedChange(task.id, !task.completed) },
                                onDelete = { viewModel.deleteTask(task.id) },
                                onClick = { navController.navigate(NewTask(task.id, task.name, task.description)) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Serializable
object Home
