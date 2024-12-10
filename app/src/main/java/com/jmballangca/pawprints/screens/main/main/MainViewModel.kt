package com.jmballangca.pawprints.screens.main.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmballangca.pawprints.repository.auth.AuthRepository
import com.jmballangca.pawprints.repository.cart.CartRepository
import com.jmballangca.pawprints.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private  val authRepository: AuthRepository,
    private val cartRepository: CartRepository
) : ViewModel() {
    var state by mutableStateOf(MainState())
    init {
        events(MainEvents.OnGetCurrentUser)
    }
    fun events(e : MainEvents) {
        when(e) {
            MainEvents.OnGetCurrentUser -> getUser()
            is MainEvents.OnGetMyCart -> getMyCart(e.uid)
        }
    }

    private fun getMyCart(uid: String) {
        viewModelScope.launch {
            cartRepository.getAllCart(uid) {
                state = when(it) {
                    is UiState.Error -> state.copy(
                        isGettingUserCart = false,
                        errors = it.message
                    )
                    UiState.Loading -> state.copy(
                        isGettingUserCart = true,
                        errors = null
                    )
                    is UiState.Success ->state.copy(
                        isGettingUserCart = false,
                        errors = null,
                        carts = it.data
                    )
                }
            }
        }
    }

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
                    is UiState.Success -> {
                        it.data?.id?.let { id->
                            events(MainEvents.OnGetMyCart(id))
                        }
                        state.copy(
                            isLoading = false,
                            errors = null,
                            users = it.data
                        )
                    }
                }
            }
        }
    }

}