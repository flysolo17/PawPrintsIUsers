package com.jmballangca.pawprints.screens.auth.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jmballangca.pawprints.R
import com.jmballangca.pawprints.router.AppRouter
import kotlinx.coroutines.delay


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    state: LoginState,
    events: (LoginEvents) -> Unit,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        events(LoginEvents.OnGetCurrentUser)
    }
    LaunchedEffect(state) {
        if (state.isLoggedIn) {
            Toast.makeText(context,"Successfully Logged in", Toast.LENGTH_SHORT).show()
            delay(1000)
            navHostController.navigate(AppRouter.MainScreen.route)
        }
        if (state.errors !== null) {
            Toast.makeText(context,state.errors, Toast.LENGTH_SHORT).show()
        }
    }
    Scaffold {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(R.drawable.paw_logo),
                contentDescription = "Logo",
                modifier =  modifier.size(100.dp)
            )

            Text(
                "PAWPRINTS",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                "Aucena Veterinary Clinic",
                style = MaterialTheme.typography.titleSmall.copy(
                    color = Color.Gray
                )
            )
            Spacer(
                modifier = modifier.height(24.dp)
            )
            OutlinedTextField(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "email"
                    )
                },
                modifier = modifier.fillMaxWidth(),
                value = state.email.value,
                onValueChange = {
                    events(LoginEvents.OnEmailChange(it))
                },
                label = { Text("Email") },
                isError = state.email.isError,
                supportingText = {
                    Text(state.email.errorMessage ?: "")
                }
            )

            OutlinedTextField(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "password"
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                value = state.password.value,
                onValueChange = {
                    events(LoginEvents.OnPasswordChange(it))
                },
                label = { Text("Password") },
                isError = state.password.isError,
                supportingText = {
                    Text(state.password.errorMessage ?: "")
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password
                )
            )
            TextButton(
                modifier = modifier.padding(
                    vertical = 8.dp
                ).align(Alignment.End),
                onClick = {
                    navHostController.navigate(AppRouter.ForgotPasswordScreen.route)
                }
            ) {
                Text("Forgot Password")
            }


            Button(
                shape = RoundedCornerShape(8.dp),
                modifier = modifier.fillMaxSize(),
                onClick = {events.invoke(LoginEvents.OnLoggedIn)},
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    Row(
                        modifier = modifier.wrapContentSize(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = modifier.size(14.dp)
                        )
                        Text(
                            "Logging in..,",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                } else {
                    Text("Login", style = MaterialTheme.typography.titleMedium)
                }
            }

            TextButton(onClick = {
                navHostController.navigate(AppRouter.RegisterScreen.route)
            }) {
                Text("No account yet ? Sign up!")
            }
        }
    }
}