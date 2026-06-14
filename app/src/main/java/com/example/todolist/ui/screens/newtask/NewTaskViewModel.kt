package com.example.todolist.ui.screens.newtask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.repository.FirebaseAuthRepository
import com.example.todolist.data.repository.FirebaseTaskRepository
import com.example.todolist.domain.repository.AuthRepository
import com.example.todolist.domain.repository.TaskRepository
import com.example.todolist.util.ValidationResult
import com.example.todolist.util.Validators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewTaskViewModel: ViewModel() {
    private val authRepository: AuthRepository = FirebaseAuthRepository()
    private val taskRepository: TaskRepository = FirebaseTaskRepository()

    private val _uiState = MutableStateFlow(NewTaskState())
    val uiState: StateFlow<NewTaskState> = _uiState.asStateFlow()

    fun onNameChange(name: String) {
        _uiState.update {
            it.copy(
                name = name,
                nameError = null,
                generalError = null
            )
        }
    }

    fun onDescriptionChange(description: String) {
        _uiState.update {
            it.copy(
                description = description,
                descriptionError = null,
                generalError = null
            )
        }
    }

    fun onSavePress(isCreate: Boolean, taskId: String? = null) {
        val state = _uiState.value

        val nameResult = Validators.validateTaskName(state.name)
        val descriptionResult = Validators.validateTaskDescription(state.description)

        if(nameResult is ValidationResult.Invalid || descriptionResult is ValidationResult.Invalid) {
            _uiState.update {
                it.copy(
                    nameError = (nameResult as? ValidationResult.Invalid)?.errorMessage,
                    descriptionError = (descriptionResult as? ValidationResult.Invalid)?.errorMessage
                )
            }
            return
        }
        if(isCreate) {
            performCreation()
        } else {
            performUpdate(taskId ?: throw Exception("Task não pode ser nulo na tela de edição"))
        }
    }

    private fun performCreation() {
        viewModelScope.launch {
            val state = _uiState.value
            try {
                _uiState.update {
                    it.copy(isLoading = true, generalError = null)
                }

                val user = authRepository.currentUserOrNull() ?: throw Exception("Erro ao capturar dados do usuário")
                taskRepository.createTask(user.uid, state.name, state.description)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        creationSuccess = true
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, generalError = e.message)
                }
            }
        }
    }

    private fun performUpdate(taskId: String) {
        viewModelScope.launch {
            val state = _uiState.value
            try {
                _uiState.update {
                    it.copy(
                        isLoading = true,
                        generalError = null
                    )
                }

                val user = authRepository.currentUserOrNull() ?: throw Exception("Erro ao capturar dados do usuário")
                taskRepository.updateTask(user.uid, taskId, name = state.name, description = state.description)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        creationSuccess = true
                    )
                }
            } catch(e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        generalError = e.message
                    )
                }
            }
        }
    }
}