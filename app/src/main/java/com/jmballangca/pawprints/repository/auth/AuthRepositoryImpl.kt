package com.jmballangca.pawprints.repository.auth

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.jmballangca.pawprints.models.users.Users
import com.jmballangca.pawprints.utils.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await

const val USER_COLLECTION = "users";
class AuthRepositoryImpl(
    private val auth : FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage : FirebaseStorage,
    ) : AuthRepository {
    override suspend fun login(
        email: String,
        password: String,
        result: (UiState<FirebaseUser>) -> Unit
    ) {
        result(UiState.Loading)
        try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            result(UiState.Success(authResult.user!!))
        } catch (e: Exception) {
            result(UiState.Error(e.message.toString()))
        }
    }

    override suspend fun register(
        user: Users,
        password: String,
        result: (UiState<String>) -> Unit
    ) {
        result(UiState.Loading)
        try {
            val authResult = auth.createUserWithEmailAndPassword(user.email!!, password).await()
            user.id = authResult.user?.uid
            firestore.collection(USER_COLLECTION).document(user.id!!).set(user).await()
            result(UiState.Success("User registered successfully"))
        } catch (e: Exception) {
            result(UiState.Error(e.message.toString()))
        }
    }

    override suspend fun getUserByID( result: (UiState<Users?>) -> Unit) {
        result(UiState.Loading)
        val currentUser = auth.currentUser
        if (currentUser == null) {
            result(UiState.Success(null))
            return
        }
        delay(1000)
        firestore.collection(USER_COLLECTION)
            .document(currentUser.uid)
            .addSnapshotListener { value, error ->
                value?.let {
                    val user = it.toObject(Users::class.java)
                    result(UiState.Success(user))
                }
                error?.let {
                    result(UiState.Error(it.message.toString()))
                }
            }
    }

    override fun forgotPassword(email: String, result: (UiState<String>) -> Unit) {
        result.invoke(UiState.Loading)
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    result.invoke(UiState.Success("We've sent a password reset link to $email"))
                } else {
                    result.invoke(UiState.Error(task.exception?.message ?: "Unknown Error"))
                }
            }
            .addOnFailureListener { exception ->
                result.invoke(UiState.Error(exception.message ?: "Unknown Error"))
            }
    }
    override suspend fun changePassword(
        oldPassword: String,
        newPassword: String,
        result: (UiState<String>) -> Unit
    ) {
        try {
            result.invoke(UiState.Loading)
            val currentUser = auth.currentUser

            if (currentUser != null) {
                val credential = EmailAuthProvider.getCredential(currentUser.email!!, oldPassword)
                currentUser.reauthenticate(credential).await()
                currentUser.updatePassword(newPassword).await()

                result.invoke(UiState.Success("Password updated successfully."))
            } else {

                result.invoke(UiState.Error("User is not logged in."))
            }
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            result.invoke(UiState.Error("Old password is incorrect."))
        } catch (e: Exception) {
            result.invoke(UiState.Error(e.message.toString()))
        }
    }

    override suspend fun updateUserInfo(
        uid: String,
        name: String,
        result: (UiState<String>) -> Unit,
    ) {
        result.invoke(UiState.Loading)
        firestore.collection(USER_COLLECTION)
            .document(uid)
            .update("name",name)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    result.invoke(UiState.Success("Successfully saved!"))
                } else {
                    result.invoke(UiState.Error(task.exception?.message ?: "Unknown Error"))
                }
            }.addOnFailureListener { exception ->
                result.invoke(UiState.Error(exception.message ?: "Unknown Error"))
            }
    }

    override fun logout(result: (UiState<String>) -> Unit) {
        result.invoke(UiState.Loading)
        auth.signOut()
        result.invoke(UiState.Success("Successfully Logged Out!"))
    }
}