package com.jmballangca.pawprints.screens.main.pets

import com.jmballangca.pawprints.models.pets.Pet
import com.jmballangca.pawprints.models.users.Users


data class PetState(
    val isLoading : Boolean=false,
    val pets : List<Pet> = emptyList(),
    val errors : String ? = null,
    val users: Users ? = null,
    val creatingPet : Boolean = false,
    val petCreated : String ? = null,
)