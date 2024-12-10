package com.jmballangca.pawprints.repository.appointment

import com.jmballangca.pawprints.models.appointment.Appointments
import com.jmballangca.pawprints.models.products.Product
import com.jmballangca.pawprints.models.schedule.ScheduleWithDoctor
import com.jmballangca.pawprints.models.users.Users
import com.jmballangca.pawprints.utils.UiState

interface AppointmentRepository {
    suspend fun createAppointment(
        user : Users,
        product: List<Product>,
        pets : List<String>,
        note : String,
        scheduleWithDoctor: ScheduleWithDoctor,
        result : (UiState<String>) -> Unit
    )

    suspend fun getMyAppointments(
        userID : String,
        result: (UiState<List<Appointments>>) -> Unit
    )
}