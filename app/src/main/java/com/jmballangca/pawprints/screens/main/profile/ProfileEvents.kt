package com.jmballangca.pawprints.screens.main.profile

import androidx.navigation.NavHostController
import com.jmballangca.pawprints.models.users.Users


sealed interface ProfileEvents  {
        data object OnLoggedOut : ProfileEvents
        data class OnSetUser(val users : Users ?) : ProfileEvents
}