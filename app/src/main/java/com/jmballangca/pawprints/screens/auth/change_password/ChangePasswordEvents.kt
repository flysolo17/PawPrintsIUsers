package com.jmballangca.pawprints.screens.auth.change_password


sealed interface ChangePasswordEvents  {
    data object OnSubmit : ChangePasswordEvents
    data class OnPasswordChanged(val password: String,val passwords: Passwords) :
        ChangePasswordEvents

    data class OnTogglePasswordVisibility(val passwords: Passwords): ChangePasswordEvents
}