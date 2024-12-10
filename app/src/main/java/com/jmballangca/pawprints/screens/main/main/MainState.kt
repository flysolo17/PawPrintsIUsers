package com.jmballangca.pawprints.screens.main.main

import com.jmballangca.pawprints.models.cart.CartWithProduct
import com.jmballangca.pawprints.models.users.Users


data class MainState(
    val isLoading : Boolean = false,
    val isGettingUserCart : Boolean = false,
    val users : Users? = null,
    val errors : String ? = null,
    val carts : List<CartWithProduct> = emptyList()
)