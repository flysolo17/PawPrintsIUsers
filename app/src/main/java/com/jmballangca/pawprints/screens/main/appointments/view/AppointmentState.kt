package com.jmballangca.pawprints.screens.main.appointments.view

import com.jmballangca.pawprints.models.appointment.Appointments
import com.jmballangca.pawprints.models.users.Users


data class AppointmentState(
    val user : Users ? = null,
    val isLoading : Boolean = false,
    val appointments : List<Appointments> = emptyList(),
    val errors : String ? = null
)