package com.example.todolist.data.repository

import com.example.todolist.domain.model.AuthUser
import com.example.todolist.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await


class FirebaseAuthRepository(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
): AuthRepository  {
    override fun currentUserOrNull(): AuthUser? = firebaseAuth.currentUser?.toAuthUser()

    override suspend fun signUp(
        fullName: String,
        email: String,
        password: String
    ): AuthUser {
        try {
            val authResult = firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .await()

            val user = authResult.user ?: throw Exception("Erro ao criar o usuário")

            val profileUpdate = UserProfileChangeRequest.Builder()
                .setDisplayName(fullName)
                .build()

            user.updateProfile(profileUpdate).await()
            user.reload().await()

            return firebaseAuth.currentUser?.toAuthUser() ?: throw Exception("Erro ao recuperar o usuário")
        } catch (e: Exception) {
            throw Exception(translateError(e, isSignUp = true))
        }
    }

    override suspend fun signIn(
        email: String,
        password: String
    ): AuthUser {
        try {
            val authResult = firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .await()
            return authResult.user?.toAuthUser() ?: throw Exception("Erro ao autenticar")
        } catch (e: Exception) {
            throw Exception(translateError(e, isSignUp = false))
        }

    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }

    private fun FirebaseUser.toAuthUser(): AuthUser = AuthUser(
        uid = uid,
        email = email,
        displayName = displayName
    )

    private fun translateError(
        error: Throwable,
        isSignUp: Boolean
    ):String {
        return when(error) {
            is FirebaseAuthWeakPasswordException -> "A senha é muito fraca. Use no mínimo seis caracteres"
            is FirebaseAuthUserCollisionException -> "Já existe uma conta com este endereço de e-mail"
            is FirebaseAuthInvalidUserException -> "Usuário não encontrado"
            is FirebaseAuthInvalidCredentialsException -> if(isSignUp) "Endereço de e-mail é inválido" else "Endereço de e-mail ou senha inválidos"
            else -> error.message ?: "Erro de autenticação. Tente Novamente"
        }
    }

}