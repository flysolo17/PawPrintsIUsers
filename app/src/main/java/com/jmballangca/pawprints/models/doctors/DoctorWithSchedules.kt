package com.jmballangca.pawprints.models.doctors

import com.jmballangca.pawprints.models.schedule.Schedule

data class DoctorWithSchedules(
    val doctors: Doctors? = null,
    val schedules : List<Schedule> = emptyList()
)
