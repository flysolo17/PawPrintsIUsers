package com.jmballangca.pawprints.repository.schedule

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.jmballangca.pawprints.models.doctors.Doctors
import com.jmballangca.pawprints.models.schedule.Schedule
import com.jmballangca.pawprints.models.schedule.ScheduleWithDoctor
import com.jmballangca.pawprints.models.schedule.getEndDayOfMonth
import com.jmballangca.pawprints.models.schedule.getStartDayOfMonth
import com.jmballangca.pawprints.utils.UiState
import kotlinx.coroutines.tasks.await
import org.threeten.bp.LocalDate

const val SCHEDULE_COLLECTION = "schedules"
const val DOCTORS_COLLECTION = "doctors"
class ScheduleRepositoryImpl(
    private val firestore : FirebaseFirestore
): ScheduleRepository {

    override suspend fun getScheduleByMonth(
        localDate: LocalDate,
        result: (UiState<List<ScheduleWithDoctor>>) -> Unit
    ) {
        result(UiState.Loading)
        try {
            val schedules = firestore.collection(SCHEDULE_COLLECTION)
                .whereGreaterThanOrEqualTo("date", localDate.getStartDayOfMonth())
                .whereLessThanOrEqualTo("date", localDate.getEndDayOfMonth())
                .get()
                .await()
            val scheduleList = schedules.toObjects(Schedule::class.java)
            Log.d("ScheduleQuery", "Number of schedules found: ${scheduleList.size}")
            Log.d("ScheduleQuery", "Query date ${localDate.getStartDayOfMonth()} - ${localDate.getEndDayOfMonth()}")
            // Print each schedule date for debugging
            scheduleList.forEach { sched ->
                Log.d("ScheduleQuery", "Schedule Date: ${sched.date}")
            }

            val schedulesWithDoctors = mutableListOf<ScheduleWithDoctor>()
            for (sched in scheduleList) {
                if (!sched.doctorID.isNullOrEmpty()) {
                    val doctor = firestore.collection(DOCTORS_COLLECTION)
                        .document(sched.doctorID)
                        .get()
                        .await()
                        .toObject(Doctors::class.java)
                    schedulesWithDoctors.add(ScheduleWithDoctor(sched, doctor))
                }
            }
            result(UiState.Success(schedulesWithDoctors))
        } catch (e: Exception) {
            Log.e("ScheduleQuery", "error",e)
            result(UiState.Error(e.message.toString()))
        }
    }
}