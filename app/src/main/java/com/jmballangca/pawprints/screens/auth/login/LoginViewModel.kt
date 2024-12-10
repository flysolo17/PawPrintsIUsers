package com.jmballangca.pawprints.screens.auth.login

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmballangca.pawprints.repository.auth.AuthRepository
import com.jmballangca.pawprints.utils.UiState
import com.jmballangca.pawprints.utils.hasSpaces
import com.jmballangca.pawprints.utils.isLessThanSix
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var state by mutableStateOf(LoginState())

    private fun getUser() {
        viewModelScope.launch {
            authRepository.getUserByID {
                state = when(it) {
                    is UiState.Error -> state.copy(
                        isLoading = false,
                        errors = it.message
                    )
                    UiState.Loading ->state.copy(
                        errors = null,
                        isLoading = true
                    )
                    is UiState.Success -> state.copy(
                        isLoading = false,
                        errors = null,
                        isLoggedIn = it.data != null
                    )
                }
            }
        }
    }

    fun events(e: LoginEvents) {
        when (e) {
            LoginEvents.OnGetCurrentUser -> {
                getUser()
            }
            LoginEvents.OnLoggedIn -> {
                login()
            }
            is LoginEvents.OnEmailChange -> {
                emailChanged(e.email)
            }
            is LoginEvents.OnPasswordChange -> {
                passwordChange(e.password)
            }
            LoginEvents.OnTogglePasswordVisibility -> state = state.copy(
                isPasswordVisible = !state.isPasswordVisible
            )
        }
    }



    private fun login() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            authRepository.login(state.email.value, state.password.value) { result ->
                state = when (result) {
                    is UiState.Success -> state.copy(isLoading = false, isLoggedIn = true)
                    is UiState.Error -> state.copy(
                        isLoading = false,
                        errors = result.message
                    )
                    UiState.Loading -> state.copy(
                        isLoading = true,
                        errors = null
                    )
                }
            }
        }
    }
    private fun passwordChange(password: String) {
        val hasError = password.isLessThanSix() || password.hasSpaces()
        val errorMessage = if (password.isLessThanSix()) {
            "Password must be at least 6 characters"
        } else if (password.hasSpaces()) {
            "Password cannot contain spaces"
        } else {
            null
        }
        val currentPassword = state.password.copy(
            value = password,
            isError = hasError,
            errorMessage = errorMessage
        )
        state = state.copy(
            password =  currentPassword
        )
    }
    private fun emailChanged(email : String) {
        val hasError =  !Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val errorMessage = if (hasError)  {
            "Invalid email"
        } else {
            null
        }
        val newEmail = state.email.copy(
            value = email,
            isError = hasError,
            errorMessage = errorMessage
        )
        state = state.copy(
            email = newEmail,
        )
    }
}