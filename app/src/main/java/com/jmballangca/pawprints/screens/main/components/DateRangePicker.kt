package com.jmballangca.pawprints.screens.main.components

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import java.util.Calendar


@Composable
fun DateRangePicker() {
    // Date state for start and end dates
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

    // Calendar instance
    val calendar = Calendar.getInstance()

    // DatePickerDialog for start date
    val startDatePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            startDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // DatePickerDialog for end date
    val endDatePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            endDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Select Date Range",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        // Button to pick start date
        Button(onClick = { startDatePickerDialog.show() }) {
            Text(text = if (startDate.isEmpty()) "Select Start Date" else "Start Date: $startDate")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button to pick end date
        Button(onClick = { endDatePickerDialog.show() }) {
            Text(text = if (endDate.isEmpty()) "Select End Date" else "End Date: $endDate")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Display selected date range
        if (startDate.isNotEmpty() && endDate.isNotEmpty()) {
            Text(
                text = "Selected Range: $startDate to $endDate",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDateRangePicker() {
    DateRangePicker()
}


