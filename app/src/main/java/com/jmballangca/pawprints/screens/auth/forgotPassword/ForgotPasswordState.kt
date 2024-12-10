package com.jmballangca.pawprints.screens.auth.forgotPassword

import com.jmballangca.pawprints.utils.Email


data class ForgotPasswordState(
    val isLoading : Boolean = false,
    val isSent : String ? = null,
    val errors : String ? = null,
    val email : Email = Email()
)