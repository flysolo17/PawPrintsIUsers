package com.jmballangca.pawprints.screens.main.appointments.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmballangca.pawprints.repository.appointment.AppointmentRepository
import com.jmballangca.pawprints.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AppointmentViewModel @Inject constructor(
    private val appointmentRepository : AppointmentRepository,

) : ViewModel() {
    var state by mutableStateOf(AppointmentState())
    fun events(e : AppointmentEvents) {
        when(e) {
            is AppointmentEvents.OnGetAllAppointments -> getAllAppointments(e.userID)
            is AppointmentEvents.OnSetUsers -> state  = state.copy(user = e.users)
        }
    }

    private fun getAllAppointments(userID: String) {
        viewModelScope.launch {
            appointmentRepository.getMyAppointments(userID) {
                state = when(it) {
                    is UiState.Error -> state.copy(
                        isLoading = false,
                        errors = it.message
                    )
                    UiState.Loading -> state.copy(
                        isLoading = true,
                        errors = null
                    )
                    is UiState.Success -> state.copy(
                        isLoading = false,
                        errors = null,
                        appointments = it.data
                    )
                }
            }
            delay(1000)
            state =state.copy(errors = null)
        }
    }
}