package com.jmballangca.pawprints.repository.schedule

import com.jmballangca.pawprints.models.schedule.ScheduleWithDoctor
import com.jmballangca.pawprints.utils.UiState
import org.threeten.bp.LocalDate


interface ScheduleRepository  {
    suspend fun getScheduleByMonth(
        localDate: LocalDate,
        result: (UiState<List<ScheduleWithDoctor>>) -> Unit
    )
}