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
            .mapNotNull { child ->
                child.getValue<TaskItem>()?.copy(id = child.key.orEmpty())
            }
            .sortedBy { it.createdAt }
    }

    override suspend fun createTask(
        uid: String,
        name: String,
        description: String
    ) {
        val ref = firebaseDatabase.getReference("users")
            .child(uid)
            .child("tasks")
            .push()
        ref.setValue(TaskItem(id = ref.key.orEmpty(), name = name, description = description))
            .await()
    }

    override suspend fun getTaskById(userUid: String, taskId: String): TaskItem {
        val snapshot = firebaseDatabase.getReference("users")
            .child(userUid)
            .child("tasks")
            .child(taskId)
            .get()
            .await()

        return snapshot.getValue<TaskItem>()?.copy(id = snapshot.key.orEmpty())
            ?: throw Exception("Tarefa não encontrada")
    }

    override suspend fun updateTask(
        userUid: String,
        taskId: String,
        name: String?,
        description: String?,
        completed: Boolean?
    ) {
        val task = getTaskById(userUid, taskId)
        val newName = name ?: task.name
        val newDescription = description ?: task.description
        val newCompleted = completed ?: task.completed

        firebaseDatabase.getReference("users")
            .child(userUid)
            .child("tasks")
            .child(taskId)
            .setValue(task.copy(name = newName, description = newDescription, completed = newCompleted))
            .await()
    }

    override suspend fun deleteTask(userUid: String, taskId: String) {
        firebaseDatabase.getReference("users")
            .child(userUid)
            .child("tasks")
            .child(taskId)
            .removeValue()
            .await()
    }
}