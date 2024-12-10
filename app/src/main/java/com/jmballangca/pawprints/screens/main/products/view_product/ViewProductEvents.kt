package com.jmballangca.pawprints.screens.main.products.view_product

import android.content.Context
import android.view.View
import androidx.navigation.NavHostController
import com.jmballangca.pawprints.models.products.Product
import com.jmballangca.pawprints.models.users.Users
import com.jmballangca.pawprints.screens.main.cart.CartEvents


sealed interface ViewProductEvents {
    data class OnGetProduct(val productID : String) : ViewProductEvents
    data class OnSetUsers(val users : Users?) : ViewProductEvents
    data class OnGetMyPets(val uid : String) : ViewProductEvents
    data class OnAddToCart(val product : Product) : ViewProductEvents

    data class OnCheckout(val navHostController: NavHostController, val context: Context):
        ViewProductEvents
}