package com.jmballangca.pawprints.screens.main.appointments.view

import com.jmballangca.pawprints.models.users.Users
import org.threeten.bp.LocalDate


sealed interface AppointmentEvents  {
    data class OnSetUsers(val users: Users) : AppointmentEvents
    data class OnGetAllAppointments(val userID : String) : AppointmentEvents

    data class OnSelectDate(val date : LocalDate) : AppointmentEvents

}