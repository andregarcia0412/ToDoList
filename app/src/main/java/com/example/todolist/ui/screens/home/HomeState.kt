package com.example.todolist.ui.screens.home

import com.example.todolist.domain.model.TaskItem

data class HomeState(
    val tasks: List<TaskItem>? = null ,
    val isLoading: Boolean = false,
    val generalError: String? = null
)
