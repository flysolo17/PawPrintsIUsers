package com.jmballangca.pawprints.screens.main.checkout

import com.jmballangca.pawprints.models.users.Users


data class CheckoutState(
    val isLoading : Boolean = false,
    val users: Users? = null,
    val errors : String? = null,
    val transactionSubmitted : String ? = null
)