package com.jmballangca.pawprints.screens.main.appointments.create.components

import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface

import androidx.compose.material3.Text

import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.jmballangca.pawprints.models.doctors.DoctorWithSchedules
import com.jmballangca.pawprints.models.schedule.ScheduleWithDoctor
import com.jmballangca.pawprints.models.schedule.display
import com.jmballangca.pawprints.ui.custom.CustomToolbar
import com.jmballangca.pawprints.ui.custom.PawPrintCalendarView
import com.jmballangca.pawprints.utils.ProfileImage
import org.threeten.bp.LocalDate

@Composable
fun CalendarViewDialog(
    modifier: Modifier = Modifier,
    date : LocalDate,
    isLoading : Boolean,
    doctorWithSchedule : List<ScheduleWithDoctor>,
    onDateSelected: (LocalDate) -> Unit,
    onMonthChange : (LocalDate) -> Unit,
    onDismiss : () -> Unit,
    onSelectSchedule : (ScheduleWithDoctor) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(modifier = modifier.fillMaxSize()) {
            LazyColumn(){
                item {
                    CustomToolbar(
                        label = "Select Schedule"
                    ) { onDismiss() }
                }
                item {
                    val scheds = doctorWithSchedule.map { it.schedule!! }
                    PawPrintCalendarView(
                        isLoading = isLoading,
                        selectedDate = date,
                        schedules = scheds,
                        onMonthChange  = {onMonthChange(it)},
                        onDateSelected= {onDateSelected(it)}
                    )
                }
                item {
                    Box(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            " Schedules for ${date.formattedDate()}",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
                val filltered = doctorWithSchedule.filter {
                    it.schedule?.date == date.formattedDate()
                }
                if (filltered.isEmpty()) {
                    item {
                        Box(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                "No Schedules",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.Gray
                                )
                            )
                        }

                    }
                }

                items(filltered) {
                    ScheduleWithDoctorCard(
                        modifier = modifier,
                        schedule = it,
                        onSelectSchedule = { onSelectSchedule(it) }
                    )
                }
            }
        }
    }
}

@Composable
fun ScheduleWithDoctorCard(
    modifier: Modifier = Modifier,
    schedule: ScheduleWithDoctor,
    onSelectSchedule: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onSelectSchedule() }
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DisplayDate(scheduleDate = schedule.schedule?.date ?: "")
            VerticalDivider()
            Column {
                Text(
                    "${schedule.schedule?.startTime?.display()} - ${schedule.schedule?.endTime?.display()}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ))

                Row(
                    modifier = modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    ProfileImage(image = schedule.doctors?.profile ?: "", modifier = modifier.size(28.dp))
                    Text("${schedule.doctors?.name}", fontWeight = FontWeight.Normal)
                }
            }

        }
    }
}
