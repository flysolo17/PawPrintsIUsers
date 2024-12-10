package com.jmballangca.pawprints.screens.main.profile

import com.jmballangca.pawprints.models.users.Users


data class ProfileState(
    val isLoading : Boolean = false,
    val users: Users? = null,
    val isLoggedOut : Boolean  = false,

    val errors : String ? = null,
    )