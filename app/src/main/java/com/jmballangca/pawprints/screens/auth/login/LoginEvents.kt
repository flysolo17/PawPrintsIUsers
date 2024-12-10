package com.jmballangca.pawprints.screens.auth.login

import android.util.Log


sealed interface LoginEvents {
    data object OnLoggedIn : LoginEvents
    data object OnGetCurrentUser : LoginEvents
    data class OnEmailChange(val email : String) : LoginEvents
    data class OnPasswordChange(val password : String) : LoginEvents
    data object OnTogglePasswordVisibility : LoginEvents
}