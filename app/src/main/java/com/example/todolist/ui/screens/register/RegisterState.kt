package com.example.todolist.ui.screens.register

data class RegisterState(
    val fullName: String = "",
    val fullNameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: String? = null,
    val generalError: String? = null,
    val isLoading: Boolean = false,
    val registerSuccess: Boolean = false
) {
    val isRegisterButtonEnabled: Boolean
        get() = !isLoading
}
