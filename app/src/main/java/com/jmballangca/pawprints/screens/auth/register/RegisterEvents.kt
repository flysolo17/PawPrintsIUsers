package com.jmballangca.pawprints.screens.auth.register



sealed interface RegisterEvents {
    data class OnNameChange(val name : String) : RegisterEvents
    data class OnEmailChange(val email : String) : RegisterEvents
    data class OnPhoneChanged(val phone : String) : RegisterEvents
    data class  OnPasswordChange(val password : String) : RegisterEvents
    data object OnRegister : RegisterEvents
}