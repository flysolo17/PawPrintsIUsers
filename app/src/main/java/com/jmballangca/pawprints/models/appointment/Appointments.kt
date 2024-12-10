package com.jmballangca.pawprints.models.appointment

import com.jmballangca.pawprints.models.schedule.Hours
import java.util.Date

data class Appointments(
    val id : String  ? = null,
    val userID : String ? = null,
    val title : String ? = null,
    val note : String ? = null,
    val attendees : List<Attendees> = emptyList(),
    val pets : List<String> = emptyList(),
    val scheduleDate : String ? =null,
    val startTime : Hours? = null,
    val endTime : Hours? = null,
    val status: AppointmentStatus? = AppointmentStatus.PENDING,
    val createdAt : Date = Date(),
    val updatedAt : Date = Date(),
)


data class Attendees(
    val id : String ? = null,
    val name : String ? = null,
    val phone : String ? = null,
    val email : String ? = null,
    val type : AttendeeType? = AttendeeType.CLIENT
)

enum class AttendeeType {
    CLIENT,
    DOCTOR
}
