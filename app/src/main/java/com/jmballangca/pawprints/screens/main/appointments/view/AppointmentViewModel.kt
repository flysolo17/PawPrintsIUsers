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
import org.threeten.bp.LocalDate
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar
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
            is AppointmentEvents.OnSelectDate -> selectDate(e.date)
        }
    }



    private fun selectDate(date: LocalDate) {
        val appointments = state.appointments
        val formatter = SimpleDateFormat("MMM, dd yyyy") // Define the format for scheduleDate

        val filtered = appointments.filter {
            val schedDate = formatter.parse(it.scheduleDate)
            val calendar = Calendar.getInstance()
            if (schedDate != null) {
                calendar.time = schedDate
            }
            val schedMonth = calendar.get(Calendar.MONTH) // Get the month from the parsed date

            schedMonth == date.monthValue - 1 // Compare the month of the scheduleDate with the selected month (monthValue is 1-based)
        }

        state = state.copy(
            filteredAppoints = filtered
        )
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
                        appointments = it.data,

                    )
                }
            }
            delay(1000)
            events(AppointmentEvents.OnSelectDate(LocalDate.now()))
            state =state.copy(errors = null)
        }
    }
}