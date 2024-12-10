package com.jmballangca.pawprints.ui.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jmballangca.pawprints.models.appointment.Appointments
import com.jmballangca.pawprints.models.schedule.Schedule
import com.jmballangca.pawprints.utils.toast
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.TextStyle
import java.util.Locale


@Composable
fun AppointmentCalendar(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    appointments: List<Appointments>,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onMonthChange: (LocalDate) -> Unit,
) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val daysInMonth = getDaysInMonthGrid(currentMonth)
    val weeks = daysInMonth.chunked(7) // Break days into weeks

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // Header with month navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = {
                currentMonth = currentMonth.minusMonths(1)
                onMonthChange(LocalDate.of(currentMonth.year, currentMonth.month, 1))
            }) {
                Text("<", fontSize = 24.sp)
            }

            val display = if (isLoading) "Getting schedule and appointments"
            else "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.year}"
            Text(
                text = display,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )

            TextButton(onClick = {
                currentMonth = currentMonth.plusMonths(1)
                onMonthChange(LocalDate.of(currentMonth.year, currentMonth.month, 1))
            }) {
                Text(">", fontSize = 24.sp)
            }
        }

        // Days of the week header
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }
        val current = LocalDate.now()
        val context = LocalContext.current
        // Calendar dates arranged in weekly rows
        weeks.forEach { week ->
            Row(modifier = Modifier.fillMaxWidth()) {
                week.forEach { date ->
                    val isSelected = date == selectedDate
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(4.dp)
                            .clickable {

                                if (date != null && date >= current) {
                                    onDateSelected(date)
                                } else {
                                    context.toast("Invalid date")
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = date?.dayOfMonth?.toString() ?: "",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = if (isSelected) MaterialTheme.colorScheme.primary
                                    else if (date != null && date.month == currentMonth.month) MaterialTheme.colorScheme.onSurface else Color.Gray,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                ),
                                textAlign = TextAlign.Center
                            )
                            Box(
                                modifier = Modifier.size(8.dp)
                                    .background(
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                        shape = CircleShape
                                    )
                            )
                        }

                    }
                }
            }
        }
    }
}

private fun getDaysInMonthGrid(yearMonth: YearMonth): List<LocalDate?> {
    val daysInMonth = mutableListOf<LocalDate?>()
    val firstDayOfMonth = yearMonth.atDay(1)
    val dayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
    val daysInPreviousMonth = dayOfWeek
    val totalDays = yearMonth.lengthOfMonth()

    for (i in 0 until daysInPreviousMonth) {
        daysInMonth.add(null)
    }

    for (day in 1..totalDays) {
        daysInMonth.add(yearMonth.atDay(day))
    }

    return daysInMonth
}
