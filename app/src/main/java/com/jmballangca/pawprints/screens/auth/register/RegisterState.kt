package com.jmballangca.pawprints.screens.auth.register

import com.jmballangca.pawprints.utils.TextFielValues


data class RegisterState(
    val isLoading : Boolean = false,
    val name :TextFielValues = TextFielValues(),
    val phone : TextFielValues = TextFielValues(),
    val email : TextFielValues = TextFielValues(),
    val password : TextFielValues = TextFielValues(),
    val errors : String ? = null,
    val registerSuccess : Boolean = false
)