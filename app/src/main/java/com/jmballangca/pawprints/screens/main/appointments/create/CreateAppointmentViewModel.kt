package com.jmballangca.pawprints.screens.main.appointments.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmballangca.pawprints.models.products.Product
import com.jmballangca.pawprints.repository.appointment.AppointmentRepository
import com.jmballangca.pawprints.repository.pets.PetRepository
import com.jmballangca.pawprints.repository.schedule.ScheduleRepository
import com.jmballangca.pawprints.screens.main.products.view_product.ViewProductEvents
import com.jmballangca.pawprints.screens.main.products.view_product.ViewProductState
import com.jmballangca.pawprints.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import javax.inject.Inject


@HiltViewModel
class CreateAppointmentViewModel @Inject constructor(
    private val petRepository: PetRepository,
    private val scheduleRepository: ScheduleRepository,
    private val appointmentRepository: AppointmentRepository
) : ViewModel() {
    var state by mutableStateOf(CreateAppointmentState())
    init {
        events(CreateAppointmentEvents.OnGetSchedules(state.selectedDate))
    }
    fun events(e : CreateAppointmentEvents) {
        when(e) {
            is CreateAppointmentEvents.OnGetSchedules -> getSchedule(e.localDate)
            is CreateAppointmentEvents.OnDateChange -> state = state.copy(selectedDate = e.localDate)

            is CreateAppointmentEvents.OnGetMyPets -> getPets(
                ownerID = e.uid)
            is CreateAppointmentEvents.OnSetUsers -> state = state.copy(
                users = e.users
            )

            is CreateAppointmentEvents.OnAddService -> addService(e.product)
            is CreateAppointmentEvents.OnSelectPets -> state = state.copy(
                selectedPets = e.pets
            )

            is CreateAppointmentEvents.OnSelectSchedule ->
                state = state.copy(
                    selectedSchedule = e.schedule
                )

            CreateAppointmentEvents.OnCreateAppointment -> createAppointment()
        }
    }

    private fun createAppointment() {
        if(state.users == null) {
            return
        }
        if (state.selectedSchedule == null) {
            return
        }
        viewModelScope.launch {
            val petIDs = state.selectedPets.map { it.id!! }
            appointmentRepository.createAppointment(
                user = state.users!!,
                product = state.selectedService,
                pets = petIDs,
                note = "",
                scheduleWithDoctor = state.selectedSchedule!!,
            ) {
                state = when(it) {
                    is UiState.Error -> state.copy(
                        isLoading = false,
                        errors = it.message
                    )
                    UiState.Loading -> state.copy(
                        isLoading = true,
                        errors = null,
                    )
                    is UiState.Success -> state.copy(
                        isLoading = false,
                        errors = null,
                        created = it.data
                    )
                }
            }
        }
    }

    private fun addService(product: Product) {
        state = state.copy(
            selectedService = state.selectedService + product
        )
    }

    private fun getPets(ownerID: String) {
        viewModelScope.launch {
            petRepository.getAllMyPet(ownerID) {
                state = when(it) {
                    is UiState.Error -> state.copy(
                        isGettingPets = false,
                        errors = it.message
                    )
                    UiState.Loading -> state.copy(
                        isGettingPets = true,
                        errors = null
                    )
                    is UiState.Success -> state.copy(
                        isGettingPets = false,
                        errors = null,
                        pets = it.data
                    )
                }
            }
        }
    }

    private fun getSchedule(localDate: LocalDate) {
        state = state.copy(scheduleWithDoctor = emptyList())
        viewModelScope.launch {
            scheduleRepository.getScheduleByMonth(localDate) {
                state = when(it) {
                    is UiState.Error -> state.copy(
                        isGettingSchedule = false,
                        errors = it.message
                    )
                    is UiState.Loading -> state.copy(
                        isGettingSchedule =true,
                        errors = null
                    )
                    is UiState.Success -> state.copy(
                        isGettingSchedule = false,
                        errors = null,
                        scheduleWithDoctor = it.data
                    )
                }
            }
        }
    }
}