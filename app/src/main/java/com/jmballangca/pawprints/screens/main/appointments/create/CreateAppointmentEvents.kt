package com.jmballangca.pawprints.screens.main.appointments.create

import com.jmballangca.pawprints.models.pets.Pet
import com.jmballangca.pawprints.models.products.Product
import com.jmballangca.pawprints.models.schedule.Schedule
import com.jmballangca.pawprints.models.schedule.ScheduleWithDoctor
import com.jmballangca.pawprints.models.users.Users
import org.threeten.bp.LocalDate


sealed interface CreateAppointmentEvents {
    data class OnSetUsers(val users : Users?) : CreateAppointmentEvents
    data class OnGetMyPets(val uid : String) : CreateAppointmentEvents
    data class OnAddService(val product: Product) : CreateAppointmentEvents
    data class OnSelectPets(val pets : List<Pet>) : CreateAppointmentEvents

    data class OnSelectSchedule(val schedule: ScheduleWithDoctor) : CreateAppointmentEvents

    data class OnGetSchedules(val localDate: LocalDate) : CreateAppointmentEvents
    data class OnDateChange(val localDate: LocalDate) : CreateAppointmentEvents
    data object OnCreateAppointment : CreateAppointmentEvents

}