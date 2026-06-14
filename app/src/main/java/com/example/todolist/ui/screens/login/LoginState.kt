package com.example.todolist.ui.screens.login

data class LoginState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val generalError: String? = null,
    val isLoading: Boolean = false
) {
    val isLoginButtonEnabled: Boolean
        get() = !isLoading
}
