package com.jmballangca.pawprints.screens.main.appointments.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jmballangca.pawprints.models.products.Product
import com.jmballangca.pawprints.screens.main.appointments.create.components.CalendarViewDialog
import com.jmballangca.pawprints.screens.main.appointments.create.components.SelectSchedule
import com.jmballangca.pawprints.screens.main.appointments.create.components.SelectedPetsCard
import com.jmballangca.pawprints.screens.main.appointments.create.components.SelectedServicesCard
import com.jmballangca.pawprints.screens.main.pets.components.SelectPetBottomDialog
import com.jmballangca.pawprints.ui.custom.CustomToolbar
import com.jmballangca.pawprints.ui.custom.PrimaryButton
import com.jmballangca.pawprints.ui.custom.ProfileImage
import com.jmballangca.pawprints.utils.Description
import com.jmballangca.pawprints.utils.RoundedImage
import com.jmballangca.pawprints.utils.Title
import com.jmballangca.pawprints.utils.toPhp
import com.jmballangca.pawprints.utils.toast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAppointmentScreen(
    modifier: Modifier = Modifier,
    product: Product?= null,
    state : CreateAppointmentState,
    events: (CreateAppointmentEvents) -> Unit,
    navHostController: NavHostController
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var showScheduleDialog by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    if (showBottomSheet) {
        SelectPetBottomDialog(
            pets = state.pets,
            sheetState = sheetState,
            onConfirm = {
                events(CreateAppointmentEvents.OnSelectPets(it))
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showBottomSheet = false
                    }
                }
            },
            onDismiss = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showBottomSheet = false
                    }
                }
            }
        )
    }
    LaunchedEffect(product) {
        if (product != null) {
            events(CreateAppointmentEvents.OnAddService(product))
        }
    }
    LaunchedEffect(Unit) {
        state.users?.id?.let {
            events(CreateAppointmentEvents.OnGetMyPets(it))
        }
    }
    LaunchedEffect(state) {
        if (state.errors != null) {
            context.toast(state.errors)
        }
        if (state.created != null) {
            context.toast(state.created)
            delay(1000)
            navHostController.popBackStack()
        }
    }


    if (showScheduleDialog) {
        CalendarViewDialog(
            date = state.selectedDate,
            isLoading = state.isGettingSchedule,
            doctorWithSchedule = state.scheduleWithDoctor,
            onDateSelected = {events(CreateAppointmentEvents.OnDateChange(it))},
            onMonthChange = {events(CreateAppointmentEvents.OnGetSchedules(it))},
            onDismiss = {  showScheduleDialog = !showScheduleDialog },
            onSelectSchedule = {
                showScheduleDialog = !showScheduleDialog
                events(CreateAppointmentEvents.OnSelectSchedule(it))
            }
        )
    }
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        item {
            CustomToolbar(
                label = "Create Appointment"
            ) { navHostController.popBackStack() }
        }

        item {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Store,
                    contentDescription = "Store",
                    modifier=modifier.size(32.dp)
                )
                Column {
                    Text(text = "Aucena Veterinary Clinic", style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ))
                    Description(
                        description = "Bonifacio San Jose Occidental Mindoro"
                    )
                }
            }
        }
        item {
            HorizontalDivider()
        }
        item {
            SelectSchedule(
                schedule = state.selectedSchedule
            ) {
                showScheduleDialog = !showScheduleDialog
            }
        }
        item {
            if (product != null) {
                SelectedServicesCard(
                    products = state.selectedService,
                    onAddService = {}
                )
            }
        }

        item {
            SelectedPetsCard(
                pets = state.selectedPets,
                isLoading = state.isGettingPets,
                onAddPet = {
                    showBottomSheet = true
                }
            )
        }
        item {
            Spacer(
                modifier = modifier.height(16.dp)
            )
        }

        item {

            PrimaryButton(
                modifier = modifier.padding(16.dp),
                onClick = {
                    if (state.users == null) {
                        context.toast("No user found!")
                        return@PrimaryButton
                    }
                    if (state.selectedPets.isEmpty()) {
                        context.toast("Select pets")
                        return@PrimaryButton
                    }
                    if (state.selectedSchedule == null) {
                        context.toast("Select Schedule")
                        return@PrimaryButton
                    }
                    events(CreateAppointmentEvents.OnCreateAppointment)
                },
                isLoading = state.isLoading
            ) {
                Text("Create")
            }
        }
    }
}
