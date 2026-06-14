package com.example.todolist.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.todolist.ui.screens.home.Home
import com.example.todolist.ui.screens.home.HomeScreen
import com.example.todolist.ui.screens.login.Login
import com.example.todolist.ui.screens.login.LoginScreen
import com.example.todolist.ui.screens.newtask.NewTask
import com.example.todolist.ui.screens.newtask.NewTaskScreen
import com.example.todolist.ui.screens.register.Register
import com.example.todolist.ui.screens.register.RegisterScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Login
    ) {
        composable<Login> {
            LoginScreen(navController)
        }

        composable<Register> {
            RegisterScreen(navController)
        }

        composable<Home> {
            HomeScreen(navController)
        }

        composable<NewTask> { backStackEntry ->
            val task = backStackEntry.toRoute<NewTask>()
            NewTaskScreen(task, navController)
        }
    }
}