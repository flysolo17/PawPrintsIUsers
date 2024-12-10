package com.jmballangca.pawprints.screens.main.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.jmballangca.pawprints.repository.auth.AuthRepository
import com.jmballangca.pawprints.screens.main.profile.ProfileState
import com.jmballangca.pawprints.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log


@HiltViewModel

class ProfileViewModel @Inject constructor(
    private  val authRepository: AuthRepository
) : ViewModel() {
    var state by mutableStateOf(ProfileState())
    init {
        viewModelScope.launch {

        }
    }
    fun events(e : ProfileEvents) {
        when(e) {
            is ProfileEvents.OnLoggedOut -> logout()
            is ProfileEvents.OnSetUser -> state = state.copy(
                users = e.users
            )
        }
    }

    private fun logout() {
        authRepository.logout {
            state = when(it) {
                is UiState.Error -> {
                    state.copy(isLoading = false, errors = it.message)
                }

                UiState.Loading -> {
                    state.copy(isLoading = true, errors = null)
                }

                is UiState.Success -> {
                    state.copy(isLoading = false, isLoggedOut = true)

                }
            }
        }
    }


}