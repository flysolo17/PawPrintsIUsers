package com.jmballangca.pawprints.screens.main.appointments.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jmballangca.pawprints.screens.main.appointments.components.AppointmentCard
import com.jmballangca.pawprints.ui.theme.PawPrintsTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentScreen(
    modifier: Modifier = Modifier,
    state: AppointmentState,
    events: (AppointmentEvents) -> Unit,
    navHostController: NavHostController
) {
    LaunchedEffect(state.user?.id) {
        state.user?.id?.let {
            events(AppointmentEvents.OnGetAllAppointments(it))
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = { Text("My Appointments") },
                navigationIcon = { IconButton(onClick = {navHostController.popBackStack()}) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                }
            )
        }
    ) {
        LazyColumn(
            modifier = modifier.fillMaxSize().padding(it)
        ) {
            if (state.isLoading) {
                item {
                   LinearProgressIndicator(
                       modifier = modifier.fillMaxWidth()
                   )
                }
            }
            items(state.appointments) {
                AppointmentCard(
                    appointments = it
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun AppointmentScreenPrev() {
    PawPrintsTheme {
        AppointmentScreen(
            state = AppointmentState(),
            events = {},
            navHostController = rememberNavController()
        )
    }
}