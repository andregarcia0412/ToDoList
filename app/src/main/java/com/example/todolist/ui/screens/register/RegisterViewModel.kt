package com.example.todolist.ui.screens.register

import androidx.lifecycle.ViewModel
import com.example.todolist.data.repository.FirebaseAuthRepository
import com.example.todolist.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RegisterViewModel: ViewModel() {
    private val authRepository: AuthRepository = FirebaseAuthRepository()

    private val _uiState = MutableStateFlow(RegisterState())
    val uiState: StateFlow<RegisterState> = _uiState.asStateFlow()
}