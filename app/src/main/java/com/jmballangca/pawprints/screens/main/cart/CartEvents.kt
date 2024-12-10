package com.jmballangca.pawprints.screens.main.cart

import android.content.Context
import androidx.navigation.NavHostController

import com.jmballangca.pawprints.models.cart.CartWithProduct
import com.jmballangca.pawprints.models.users.Users


sealed interface CartEvents {
    data class OnUpdateCart(val carts : List<CartWithProduct>) : CartEvents

    data class OnIncrease(val id : String) : CartEvents
    data class OnDecrease(val id : String) : CartEvents
    data class OnSelect(val id : String) : CartEvents
    data class OnSelectAll(val current : Boolean): CartEvents
    data class OnCheckout(val navHostController: NavHostController,val context: Context): CartEvents

    data class OnSetUsers(val users : Users?) : CartEvents
}