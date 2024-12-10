package com.jmballangca.pawprints.screens.auth.login

import com.jmballangca.pawprints.utils.TextFielValues


data class LoginState(
    val isLoading : Boolean = false,
    val email : TextFielValues = TextFielValues(),
    val password : TextFielValues = TextFielValues(),
    val isPasswordVisible : Boolean = false,
    val errors : String ?= null,
    val isLoggedIn : Boolean =false
)