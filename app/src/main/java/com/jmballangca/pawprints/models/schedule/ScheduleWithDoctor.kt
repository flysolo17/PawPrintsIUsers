package com.jmballangca.pawprints.models.schedule

import com.jmballangca.pawprints.models.doctors.Doctors


data class ScheduleWithDoctor(
    val schedule: Schedule? = null,
    val doctors: Doctors? = null,
)