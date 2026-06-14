package com.example.todolist.ui.screens.newtask

data class NewTaskState(
    val name: String = "",
    val nameError: String? = null,
    val description: String = "",
    val descriptionError: String? = null,
    val generalError: String? = null,
    val isLoading: Boolean = false,
    val creationSuccess: Boolean = false
) {
    val isSaveButtonEnabled: Boolean
        get() = !isLoading
}
