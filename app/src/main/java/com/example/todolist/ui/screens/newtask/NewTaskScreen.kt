package com.example.todolist.ui.screens.newtask

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todolist.ui.components.GenericButton
import com.example.todolist.ui.components.Input
import com.example.todolist.ui.components.LogoBox
import com.example.todolist.ui.components.TopBarDefault
import com.example.todolist.ui.screens.home.Home
import kotlinx.serialization.Serializable

@Composable
fun NewTaskScreen(newTask: NewTask, navController: NavController) {
    val viewModel: NewTaskViewModel = viewModel()
    val screenState = viewModel.uiState.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.onNameChange(newTask.name ?: "")
        viewModel.onDescriptionChange(newTask.description ?: "")
    }

    LaunchedEffect(screenState.creationSuccess) {
        if (screenState.creationSuccess) {
            navController.navigate(Home) {
                popUpTo<Home> { inclusive = true }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFF131313),
        topBar = {
            TopBarDefault(
                title = if (newTask.name != null) "Editar ${newTask.name}" else "Nova Tarefa"
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Input(
                    label = "Nome",
                    placeholder = "Digite o nome da tarefa",
                    value = screenState.name,
                    onValueChange = { viewModel.onNameChange(it) },
                    error = screenState.nameError
                )

                Input(
                    label = "Descrição",
                    placeholder = "Digite a descrição da tarefa",
                    value = screenState.description,
                    onValueChange = { viewModel.onDescriptionChange(it) },
                    error = screenState.descriptionError
                )
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 50.dp)
                    .padding(top = 10.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                screenState.generalError?.let {
                    Text(
                        text = it,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 14.sp,
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                GenericButton(
                    text = "Salvar",
                    onClick = { viewModel.onSavePress() },
                    containerColor = Color(0xFF0F62FE),
                    contentColor = Color.White,
                    enabled = screenState.isSaveButtonEnabled
                )

                GenericButton(
                    text = "Cancelar",
                    onClick = { navController.navigate(Home) {
                        popUpTo<Home> { inclusive = true }
                    } },
                    containerColor = Color.Transparent,
                    contentColor = Color.White,
                    border = BorderStroke(1.dp, Color.White)
                )
            }
        }
    }
}

@Serializable
data class NewTask(
    val name: String? = null,
    val description: String? = null,
)
