package com.jmballangca.pawprints.screens.main.appointments.create

import com.jmballangca.pawprints.models.pets.Pet
import com.jmballangca.pawprints.models.products.Product
import com.jmballangca.pawprints.models.schedule.Schedule
import com.jmballangca.pawprints.models.schedule.ScheduleWithDoctor
import com.jmballangca.pawprints.models.users.Users
import org.threeten.bp.LocalDate


data class CreateAppointmentState(
    val isLoading : Boolean = false,
    val errors : String ? = null,
    val users : Users? = null,
    val isGettingPets : Boolean = false,
    val pets : List<Pet> = emptyList(),
    val selectedSchedule: ScheduleWithDoctor? = null,
    val selectedPets : List<Pet> = emptyList(),
    val selectedService : List<Product> = emptyList(),

    val isGettingSchedule : Boolean = false,
    val selectedDate : LocalDate = LocalDate.now(),
    val scheduleWithDoctor : List<ScheduleWithDoctor> = emptyList(),
    val created : String ? = null,
)