package com.example.todolist.ui.screens.register

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

class RegisterViewModel: ViewModel() {
    private val authRepository: AuthRepository = FirebaseAuthRepository()

    private val _uiState = MutableStateFlow(RegisterState())
    val uiState: StateFlow<RegisterState> = _uiState.asStateFlow()

    fun onFullNameChange(fullName: String) {
        _uiState.update {
            it.copy(fullName = fullName, fullNameError = null, generalError = null)
        }
    }

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

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.update {
            it.copy(confirmPassword = confirmPassword, confirmPasswordError = null, generalError = null)
        }
    }

    fun onRegisterPress() {
        val state = _uiState.value

        val fullNameResult = Validators.validateFullName(state.fullName)
        val emailResult = Validators.validateEmail(state.email)
        val passwordResult = Validators.validatePassword(state.password)
        val confirmPasswordResult = Validators.validatePasswordConfirmation(state.password, state.confirmPassword)

        if(
            fullNameResult is ValidationResult.Invalid ||
            emailResult is ValidationResult.Invalid ||
            passwordResult is ValidationResult.Invalid ||
            confirmPasswordResult is ValidationResult.Invalid
            ) {
            _uiState.update {
                it.copy(
                    fullNameError = (fullNameResult as? ValidationResult.Invalid)?.errorMessage,
                    emailError = (emailResult as? ValidationResult.Invalid)?.errorMessage,
                    passwordError = (passwordResult as? ValidationResult.Invalid)?.errorMessage,
                    confirmPasswordError = (passwordResult as? ValidationResult.Invalid)?.errorMessage
                )
            }
            return
        }

        performRegister()
    }

    private fun performRegister() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, generalError = null) }

            val state = _uiState.value

            try {
                authRepository.signUp(state.fullName, state.email, state.password)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        registerSuccess = true
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