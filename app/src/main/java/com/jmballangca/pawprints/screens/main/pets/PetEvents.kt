package com.jmballangca.pawprints.screens.main.pets

import android.net.Uri
import com.jmballangca.pawprints.models.pets.Pet
import com.jmballangca.pawprints.models.users.Users


sealed interface PetEvents {
    data class OnGetPets(val ownerID : String) : PetEvents
    data class OnSetUser(val users: Users ?) : PetEvents
    data class OnCreatePet(
        val pet : Pet,
        val uri : Uri,

    ) : PetEvents
}