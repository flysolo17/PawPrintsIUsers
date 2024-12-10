package com.jmballangca.pawprints.screens.main.products.view_product

import com.jmballangca.pawprints.models.pets.Pet
import com.jmballangca.pawprints.models.products.Product
import com.jmballangca.pawprints.models.users.Users


data class ViewProductState(
    val isLoading : Boolean = false,
    val product: Product ? = null,
    val errors : String ? = null,
    val users : Users ? = null,
    val isGettingPets : Boolean = false,
    val pets : List<Pet> = emptyList(),

    val adding : Boolean = false,
    val cartAdded : String ? = null,
)