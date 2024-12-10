package com.jmballangca.pawprints.repository.appointment

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jmballangca.pawprints.models.appointment.AppointmentStatus
import com.jmballangca.pawprints.models.appointment.Appointments
import com.jmballangca.pawprints.models.appointment.AttendeeType
import com.jmballangca.pawprints.models.appointment.Attendees
import com.jmballangca.pawprints.models.appointment.Inbox
import com.jmballangca.pawprints.models.appointment.InboxTpe
import com.jmballangca.pawprints.models.products.Product
import com.jmballangca.pawprints.models.products.getNames
import com.jmballangca.pawprints.models.schedule.Hours
import com.jmballangca.pawprints.models.schedule.ScheduleWithDoctor
import com.jmballangca.pawprints.models.schedule.display
import com.jmballangca.pawprints.models.users.Users
import com.jmballangca.pawprints.repository.schedule.SCHEDULE_COLLECTION
import com.jmballangca.pawprints.utils.UiState
import com.jmballangca.pawprints.utils.generateRandomNumberString
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
const val INBOX_COLLECTION = "inbox"
const val APPOINTMENTS = "appointments"
class AppointmentRepositoryImpl(
    private val firestore: FirebaseFirestore,
): AppointmentRepository {

    override suspend fun createAppointment(
        user: Users,
        product: List<Product>,
        pets : List<String>,
        note : String,
        scheduleWithDoctor: ScheduleWithDoctor,
        result: (UiState<String>) -> Unit
    ) {
        try {
            result.invoke(UiState.Loading)
            val batch = firestore.batch()
            val users = Attendees(
                id = user.id,
                name = user.name,
                phone = user.phone,
                email = user.email,
                type = AttendeeType.CLIENT
            )

            val doctor = Attendees(
                id = scheduleWithDoctor.doctors?.id,
                name = scheduleWithDoctor.doctors?.name,
                phone = scheduleWithDoctor.doctors?.phone,
                email = scheduleWithDoctor.doctors?.email,
                type = AttendeeType.DOCTOR
            )
            val appointments = Appointments(
                id = generateRandomNumberString(12),
                userID = user.id,
                title = product.getNames(),
                note = note,
                attendees = listOf(users, doctor),
                pets = pets,
                scheduleDate = scheduleWithDoctor.schedule?.date,
                startTime = scheduleWithDoctor.schedule?.startTime,
                endTime = scheduleWithDoctor.schedule?.endTime,
            )
            val inbox = Inbox(
                id = generateRandomNumberString(8),
                userID = user.id,
                collectionID = appointments.id,
                type = InboxTpe.APPOINTMENTS,
                message = "Appointment created"
            )
            val appointmentRef = firestore.collection(APPOINTMENTS)
                .document(appointments.id!!)
            val inboxRef = firestore.collection(INBOX_COLLECTION)
                .document(inbox.id!!)

            val scheduleRef = firestore.collection(SCHEDULE_COLLECTION)
                .document(scheduleWithDoctor.schedule?.id!!)
            batch.set(appointmentRef, appointments)
            batch.set(inboxRef,inbox)
            batch.update(
                scheduleRef,
                "slots", FieldValue.increment(-1)
            )
            batch.commit()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        result.invoke(UiState.Success("Successfully created!"))
                    } else {
                        result.invoke(UiState.Error("Error Creating appointment"))
                    }
                }.addOnFailureListener {
                    Log.e("appointment",it.message,it)
                    result.invoke(UiState.Error(it.message.toString()))
                }
        } catch (e : Exception) {
            Log.e("appointment",e.message,e)
            result.invoke(UiState.Error(e.message.toString()))
        }
    }

    override suspend fun getMyAppointments(
        userID: String,
        result: (UiState<List<Appointments>>) -> Unit
    ) {
        result.invoke(UiState.Loading)
        delay(1000)
        firestore.collection(APPOINTMENTS)
            .whereEqualTo("userID",userID)
            .orderBy("createdAt",Query.Direction.DESCENDING)
            .orderBy("updatedAt",Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                value?.let {
                    result.invoke(UiState.Success(it.toObjects(Appointments::class.java)))
                }
                error?.let{ Log.e("appointment",it.message,it)
                    result.invoke(UiState.Error(it.message.toString()))
                }
            }
    }
}