package com.jmballangca.pawprints.screens.main.home

import com.jmballangca.pawprints.models.products.Product
import com.jmballangca.pawprints.models.products.ProductType
import com.jmballangca.pawprints.models.users.Users


sealed interface HomeEvents  {
    data object OnGetAllProducts : HomeEvents
    data class OnSetUser(val users : Users? ) : HomeEvents
    data class AddToCart(val product: Product) : HomeEvents

    data class OnTabSelected(val tab : ProductType) : HomeEvents

    data class OnGetAppointments(val userID : String) : HomeEvents
}