package com.jmballangca.pawprints.screens.main.pets

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmballangca.pawprints.models.pets.Pet
import com.jmballangca.pawprints.repository.pets.PetRepository
import com.jmballangca.pawprints.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PetViewModel @Inject constructor(
    private val petRepository: PetRepository
) : ViewModel() {
    var state by mutableStateOf(PetState())

    fun events(e : PetEvents) {
        when(e) {
            is PetEvents.OnGetPets ->getPets(e.ownerID)
            is PetEvents.OnSetUser -> state = state.copy(
                users = e.users
            )

            is PetEvents.OnCreatePet -> createPet(
                e.pet,
                e.uri
            )
        }
    }

    private fun createPet(pet: Pet, uri: Uri) {
        viewModelScope.launch {
            petRepository.createPet(pet,uri) {
                state = when(it) {
                    is UiState.Error -> state.copy(
                        creatingPet = false,
                        errors = it.message,

                    )
                    UiState.Loading -> state.copy(
                        isLoading = true,
                        errors = null,
                    )
                    is UiState.Success -> state.copy(
                        isLoading = false,
                        errors = null,
                        petCreated = it.data
                    )
                }
            }
            delay(1000)
            state = state.copy(petCreated = null)
        }
    }

    private fun getPets(ownerID: String) {
        viewModelScope.launch {
            petRepository.getAllMyPet(ownerID) {
                state = when(it) {
                    is UiState.Error -> state.copy(
                        isLoading = false,
                        errors = it.message
                    )
                    UiState.Loading -> state.copy(
                        isLoading = true,
                        errors = null
                    )
                    is UiState.Success -> state.copy(
                        isLoading = false,
                        errors = null,
                        pets = it.data
                    )
                }
            }
        }
    }
}