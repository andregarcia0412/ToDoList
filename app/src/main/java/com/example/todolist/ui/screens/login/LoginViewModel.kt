package com.example.todolist.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.repository.FirebaseAuthRepository
import com.example.todolist.domain.repository.AuthRepository
import com.example.todolist.util.ValidationResult
import com.example.todolist.util.Validators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    private val authRepository: AuthRepository = FirebaseAuthRepository()
    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(email = email, emailError = null, generalError = null)
        }
    }

    fun onPasswordChange(password: String) {
        _uiState.update {
            it.copy(password = password, passwordError = null, generalError = null)
        }
    }

    fun onLoginPress() {
        val state = _uiState.value

        val emailResult = Validators.validateEmail(state.email)
        val passwordResult = Validators.validatePassword(state.password)

        if(emailResult is ValidationResult.Invalid || passwordResult is ValidationResult.Invalid) {
            _uiState.update {
                it.copy(
                    emailError = (emailResult as? ValidationResult.Invalid)?.errorMessage,
                    passwordError = (passwordResult as? ValidationResult.Invalid)?.errorMessage
                )
            }
            return
        }

        performLogin()
    }

    private fun performLogin() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, generalError = null) }

            val state = _uiState.value

            try {
                authRepository.signIn(state.email, state.password)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        loginSuccess = true
                    )
                }
            } catch (e: Exception) {
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