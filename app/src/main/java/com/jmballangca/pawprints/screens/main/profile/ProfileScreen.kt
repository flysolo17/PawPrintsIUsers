package com.jmballangca.pawprints.screens.main.profile

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jmballangca.pawprints.router.AppRouter
import com.jmballangca.pawprints.ui.custom.PrimaryButton
import com.jmballangca.pawprints.ui.custom.ProfileImage


@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    mainNav : NavHostController,
    navHostController: NavHostController,
    state: ProfileState,
    events: (ProfileEvents) -> Unit
) {
    val  context = LocalContext.current
    LaunchedEffect(state) {
        if (state.isLoggedOut) {
            Toast.makeText(context,"Successfully Logged Out", Toast.LENGTH_SHORT).show()
            mainNav.navigate(AppRouter.AuthRoutes.route)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ProfileLayout(state = state)
        Spacer(modifier = modifier.weight(1f))
        PrimaryButton(
            onClick = {
                navHostController.navigate(AppRouter.EditProfileRoute.navigate(state.users!!))
            }) {
            Text(text = "Edit Profile")
        }
        PrimaryButton(onClick = { navHostController.navigate(AppRouter.ChangePassword.route) }) {
            Text(text = "Change Password")
        }
        PrimaryButton(onClick = { events(ProfileEvents.OnLoggedOut) }, isLoading = state.isLoading) {
            Text(text = "Logout")
        }
    }
}


@Composable
fun ProfileLayout(
    modifier: Modifier = Modifier,
    state: ProfileState,
) {
    val users = state.users
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileImage(imageURL = users?.profile ?: "", size = 80.dp) {

        }
        Text(text = "${users?.name}", style = MaterialTheme.typography.titleLarge)
    }
}
