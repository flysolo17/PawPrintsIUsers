package com.jmballangca.pawprints.screens.main.appointments.create.components

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale



public fun org.threeten.bp.LocalDate.formattedDate(): String {
  val formatter =  org.threeten.bp.format.DateTimeFormatter.ofPattern("MMM, dd yyyy")
  return  this.format(formatter)
}
@Composable
fun DisplayDate(scheduleDate: String) {
   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
       val formatter =  DateTimeFormatter.ofPattern("MMM, dd yyyy")
       val date = LocalDate.parse(scheduleDate, formatter)

       val day = date.dayOfMonth.toString()
       val month = date.month.getDisplayName(java.time.format.TextStyle.SHORT, Locale.ENGLISH)
       val annotatedString = buildAnnotatedString {
           withStyle(style = SpanStyle(
               color = MaterialTheme.colorScheme.primary,
               fontSize = 18.sp,
               fontWeight = FontWeight.Black,

               )
           ) {
               append(day)
           }
           append("\n")
           withStyle(
               style = SpanStyle(
                   fontWeight = FontWeight.Black,
               )) {
               append(month)
           }

       }

       Text(
           text = annotatedString,
           textAlign = TextAlign.Center
       )
    } else {

       Text(
           text = "Invalid",
           textAlign = TextAlign.Center
       )
    }

}