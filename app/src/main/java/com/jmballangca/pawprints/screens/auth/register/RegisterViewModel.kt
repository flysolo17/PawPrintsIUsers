package com.jmballangca.pawprints.screens.auth.register

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmballangca.pawprints.models.users.Users
import com.jmballangca.pawprints.repository.auth.AuthRepository
import com.jmballangca.pawprints.utils.UiState
import com.jmballangca.pawprints.utils.hasNumbers
import com.jmballangca.pawprints.utils.hasSpaces
import com.jmballangca.pawprints.utils.isLessThanSix
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var state by mutableStateOf(RegisterState())
    fun events(e : RegisterEvents) {
        when(e) {
            is RegisterEvents.OnEmailChange -> emailChanged(
                email = e.email
            )
            is RegisterEvents.OnNameChange -> nameChanged(
                name =e.name
            )
            is RegisterEvents.OnPasswordChange -> passwordChange(
                password = e.password
            )
            RegisterEvents.OnRegister -> register()
            is RegisterEvents.OnPhoneChanged -> phoneChange(e.phone)
        }
    }

    private fun phoneChange(phone: String) {
        val hasError = phone.hasSpaces()
        val errorMessage = if (hasError)  {
            "Invalid phone"
        } else {
            null
        }
        state =  state.copy(
            phone =state.phone.copy(
                value = phone,
                isError = hasError,
                errorMessage = errorMessage
            ),
        )
    }

    private fun register() {
        val name : String = state.name.value
        val email : String = state.email.value
        val password : String = state.password.value
        val users : Users = Users(
            name = name,
            email = email,
        )
        viewModelScope.launch {
            authRepository.register(user = users, password = password) {
                when(it) {
                    is UiState.Error -> {
                        state = state.copy(
                            isLoading = false,
                            errors = it.message
                        )
                    }
                    is UiState.Loading -> {
                        state = state.copy(
                            isLoading = true,
                            errors = null,
                        )
                    }

                    is UiState.Success -> {
                        state = state.copy(
                            isLoading = false,
                            registerSuccess = true,
                            errors = null
                        )
                    }
                }
            }
        }
    }

    private fun nameChanged(name : String) {
        val hasError = name.hasNumbers()
        val errorMessage = if (hasError)  {
            "Invalid name"
        } else {
            null
        }
        state =  state.copy(
            name =state.name.copy(
                value = name,
                isError = hasError,
                errorMessage = errorMessage
            ),
        )
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