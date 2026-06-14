package com.example.todolist.data.repository

import com.example.todolist.domain.model.TaskItem
import com.example.todolist.domain.repository.TaskRepository
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import kotlinx.coroutines.tasks.await

class FirebaseTaskRepository(
    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
): TaskRepository {
    override suspend fun getTasksByUserUid(uid: String): List<TaskItem> {
        val snapshot = firebaseDatabase
            .getReference("users")
            .child(uid)
            .child("tasks")
            .get()
            .await()

        return snapshot.children
            .mapNotNull { it.getValue<TaskItem>() }
            .sortedBy { it.createdAt }
    }

    override suspend fun createTask(
        uid: String,
        name: String,
        description: String
    ) {
        firebaseDatabase.getReference("users")
            .child(uid)
            .child("tasks")
            .push()
            .setValue(TaskItem(name, description, false, System.currentTimeMillis()))
            .await()
    }

    override suspend fun getTaskByUid(userUid: String, taskUid: String): TaskItem {
        val snapshot = firebaseDatabase.getReference("users")
            .child(userUid)
            .child("tasks")
            .child(taskUid)
            .get()
            .await()

        return snapshot.getValue<TaskItem>() ?: throw Exception("Tarefa não encontrada")
    }

    override suspend fun updateTask(
        userUid: String,
        taskUid: String,
        name: String?,
        description: String?,
        completed: Boolean?
    ) {
        val task = getTaskByUid(userUid, taskUid)
        val newName = name ?: task.name
        val newDescription = description ?: task.description
        val newCompleted = completed ?: task.completed

        firebaseDatabase.getReference("users")
            .child(userUid)
            .child("tasks")
            .child(taskUid)
            .push()
            .setValue(task.copy(name = newName, description = newDescription, completed = newCompleted))
    }

    override suspend fun deleteTask(userUid: String, taskUid: String) {
        firebaseDatabase.getReference("users")
            .child(userUid)
            .child("tasks")
            .child(taskUid)
            .removeValue()
            .await()
    }
}