package com.jmballangca.pawprints.screens.main.home

import com.jmballangca.pawprints.models.appointment.Appointments
import com.jmballangca.pawprints.models.products.Product
import com.jmballangca.pawprints.models.products.ProductType
import com.jmballangca.pawprints.models.users.Users


data class HomeState(
    val isLoading : Boolean = false,
    val products : List<Product> = emptyList(),
    val errors : String? = null,
    val users : Users? = null,
    val adding: String ? = null,
    val cartAdded : String ? = null,
    val selectedTab : ProductType = ProductType.SERVICES,
    val appointments : List<Appointments> = emptyList(),
    val isGettingAppointments : Boolean = true
)