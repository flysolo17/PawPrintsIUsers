package com.jmballangca.pawprints.screens.auth.edit_profile


sealed interface EditProfileEvents  {

    data class OnNameChange(val name : String) : EditProfileEvents

    data class OnSaveChanges(
        val uid : String,
        val name : String,


    ) : EditProfileEvents
}