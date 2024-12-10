package com.jmballangca.pawprints.screens.main.appointments.create.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CalendarViewDay
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jmballangca.pawprints.models.schedule.Schedule
import com.jmballangca.pawprints.models.schedule.ScheduleWithDoctor


@Composable
fun SelectSchedule(
    modifier: Modifier = Modifier,
    schedule: ScheduleWithDoctor?,
    onSelectSchedule : () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = modifier.fillMaxWidth().padding(8.dp)
    ) {
        Column(
            modifier = modifier.fillMaxWidth().padding(16.dp)
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Scheduled date",
                    style = MaterialTheme.typography.titleMedium
                )
                OutlinedButton(
                    onClick = onSelectSchedule,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = modifier
                        .padding(4.dp)
                        .height(32.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CalendarToday,
                            contentDescription = "Select Schedule",
                            modifier = modifier.size(16.dp)
                        )
                        Text(
                            text = "Select Schedule",
                            fontSize = 12.sp
                        )
                    }
                }
            }

            if (schedule == null) {
                Text(
                    text = "No selected Schedule",
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center,
                    modifier = modifier.fillMaxWidth().padding(16.dp)
                )
            } else {
                ScheduleWithDoctorCard(schedule = schedule, onSelectSchedule = {})
            }
        }
    }
}