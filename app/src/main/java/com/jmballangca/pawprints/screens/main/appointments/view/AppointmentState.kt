package com.jmballangca.pawprints.screens.main.appointments.view

import com.jmballangca.pawprints.models.appointment.Appointments
import com.jmballangca.pawprints.models.users.Users
import org.threeten.bp.LocalDate
import java.util.Date


data class AppointmentState(
    val user : Users ? = null,
    val isLoading : Boolean = false,
    val selectedDate : LocalDate = LocalDate.now(),
    val appointments : List<Appointments> = emptyList(),
    val filteredAppoints : List<Appointments> = emptyList(),
    val errors : String ? = null
)