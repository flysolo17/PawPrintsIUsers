package com.jmballangca.pawprints.screens.auth.edit_profile

import android.net.Uri
import androidx.compose.ui.text.input.TextFieldValue
import com.jmballangca.pawprints.utils.Fullname


data class EditProfileState(
    val isLoading : Boolean = false,
    val name : Fullname = Fullname(),

    val errors : String ? = null,
    val isSaving : Boolean = false,
    val isDoneSaving : String ? = null
)