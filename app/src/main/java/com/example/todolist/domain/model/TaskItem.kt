package com.example.todolist.domain.model

data class TaskItem(
    val name: String? = "",
    val description: String? = "",
    val completed: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
