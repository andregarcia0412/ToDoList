package com.example.todolist.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.repository.FirebaseAuthRepository
import com.example.todolist.data.repository.FirebaseTaskRepository
import com.example.todolist.domain.repository.AuthRepository
import com.example.todolist.domain.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val authRepository: AuthRepository = FirebaseAuthRepository()
    private val taskRepository: TaskRepository = FirebaseTaskRepository()
    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()

    init {
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(isLoading = true)
                }

                val currentUser = authRepository.currentUserOrNull()
                    ?: throw Exception("Erro ao capturar dados do usuário atual")
                val tasks = taskRepository.getTasksByUserUid(currentUser.uid)

                _uiState.update {
                    it.copy(isLoading = false, generalError = null, tasks = tasks)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, generalError = e.message)
                }
            }
        }
    }

    fun onCheckedChange(taskId: String, checked: Boolean) {
        val tasks = _uiState.value.tasks ?: return

        _uiState.update { state ->
            state.copy(tasks = tasks.map { if (it.id == taskId) it.copy(completed = checked) else it })
        }

        viewModelScope.launch {
            try {
                val user = authRepository.currentUserOrNull()
                    ?: throw Exception("Erro ao capturar dados do usuário atual")
                taskRepository.updateTask(user.uid, taskId, completed = checked)
            } catch (e: Exception) {
                _uiState.update { state ->
                    state.copy(tasks = state.tasks?.map {
                        if (it.id == taskId) it.copy(completed = !checked) else it
                    })
                }
            }
        }
    }

    fun deleteTask(taskId: String) {
        val tasks = _uiState.value.tasks ?: return

        _uiState.update { state ->
            state.copy(tasks = tasks.filter { it.id != taskId })
        }

        viewModelScope.launch {
            try {
                val user = authRepository.currentUserOrNull()
                    ?: throw Exception("Erro ao capturar dados do usuário atual")
                taskRepository.deleteTask(user.uid, taskId)
            } catch (e: Exception) {
                _uiState.update { state ->
                    state.copy(tasks = tasks)
                }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
        }
    }
}