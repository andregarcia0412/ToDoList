package com.example.todolist.domain.repository

import com.example.todolist.domain.model.TaskItem

interface TaskRepository {
    suspend fun getTasksByUserUid(uid: String): List<TaskItem>

    suspend fun createTask(uid: String, name: String, description: String)

    suspend fun getTaskByUid(userUid: String, taskUid: String): TaskItem

    suspend fun updateTask(userUid: String, taskUid: String, name: String? = null, description: String? = null, completed: Boolean? = null)

    suspend fun deleteTask(userUid: String, taskUid: String)
}