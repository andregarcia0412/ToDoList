package com.example.todolist.ui.screens.login

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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todolist.ui.components.GenericButton
import com.example.todolist.ui.components.Input
import com.example.todolist.ui.components.LogoBox
import com.example.todolist.ui.screens.home.Home
import com.example.todolist.ui.screens.register.Register
import kotlinx.serialization.Serializable

@Composable
fun LoginScreen(navController: NavController) {
    val viewModel: LoginViewModel = viewModel()
    val screenState = viewModel.uiState.collectAsState().value

    LaunchedEffect(screenState.loginSuccess) {
        if(screenState.loginSuccess) {
            navController.navigate(Home) {
                popUpTo<Login> { inclusive = true }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFF131313)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(90.dp))

            LogoBox()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Input(
                    label = "Email",
                    placeholder = "Digite seu email",
                    value = screenState.email,
                    onValueChange = { viewModel.onEmailChange(it) },
                    error = screenState.emailError
                )

                Input(
                    label = "Senha",
                    placeholder = "Digite sua senha",
                    value = screenState.password,
                    onValueChange = { viewModel.onPasswordChange(it) },
                    isPassword = true,
                    error = screenState.passwordError
                )
            }

            Column(modifier = Modifier
                .padding(horizontal = 50.dp)
                .padding(top=10.dp)) {

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
                    text = "Entrar",
                    onClick = { viewModel.onLoginPress() },
                    containerColor = Color(0xFF0F62FE),
                    contentColor = Color.White,
                    enabled = screenState.isLoginButtonEnabled
                )
            }

            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp)
                .padding(top = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextButton(
                    onClick = {
                        navController.navigate(Register)
                    },
                ) {
                    Text(
                        text = "Não tenho uma conta",
                        color = Color(0xFF3A91FF),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }

}

@Serializable
object Login