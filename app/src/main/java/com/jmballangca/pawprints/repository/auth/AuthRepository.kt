package com.jmballangca.pawprints.repository.auth

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.auth.User
import com.jmballangca.pawprints.models.users.Users
import com.jmballangca.pawprints.utils.UiState

interface AuthRepository {

    suspend fun login(
        email : String,
        password : String,
        result: (UiState<FirebaseUser>) -> Unit
    )
    suspend fun register(
        user: Users,
        password: String,
        result: (UiState<String>) -> Unit
    )

    suspend fun getUserByID(result: (UiState<Users?>) -> Unit)


    fun forgotPassword(email : String,result: (UiState<String>) -> Unit)

    suspend fun changePassword(oldPassword : String,newPassword : String,result: (UiState<String>) -> Unit)
    suspend fun updateUserInfo(
        uid : String,
        name : String,
        result: (UiState<String>) -> Unit
    )
    fun logout(
        result: (UiState<String>) -> Unit
    )
}