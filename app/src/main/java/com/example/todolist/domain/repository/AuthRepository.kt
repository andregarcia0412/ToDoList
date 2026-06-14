package com.example.todolist.domain.repository

import com.example.todolist.domain.model.AuthUser

interface AuthRepository {
    fun currentUserOrNull(): AuthUser?

    suspend fun signUp(
        fullName: String,
        email: String,
        password: String
    ): AuthUser

    suspend fun signIn(email: String, password: String): AuthUser

    suspend fun signOut()
}