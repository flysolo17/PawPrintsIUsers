package com.jmballangca.pawprints.repository.pets

import android.net.Uri
import com.jmballangca.pawprints.models.pets.Pet
import com.jmballangca.pawprints.utils.UiState


interface PetRepository {
    suspend fun createPet(
        pet: Pet,
        uri: Uri,
        result : (UiState<String>) -> Unit
    )

    suspend fun getAllMyPet(
        uid : String,
        result: (UiState<List<Pet>>) -> Unit
    )
}