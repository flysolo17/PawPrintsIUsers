package com.jmballangca.pawprints.screens.main.cart

import com.jmballangca.pawprints.models.cart.CartWithProduct
import com.jmballangca.pawprints.models.users.Users


data class CartState(
    val isLoading : Boolean = false,
    val carts : List<CartWithProduct> = emptyList(),
    val isUpdating : String ? = null,
    val updated  : String ? = null,
    val errors : String ? = null,
    val selectedCartId : List<String> = emptyList(),
    val total : Double = 0.00,

    val selectAll : Boolean = false,
    val users : Users? = null,
)