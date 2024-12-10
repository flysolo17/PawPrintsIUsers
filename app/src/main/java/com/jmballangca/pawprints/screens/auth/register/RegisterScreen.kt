package com.jmballangca.pawprints.screens.auth.register

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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.jmballangca.pawprints.R

import kotlinx.coroutines.delay


@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    state : RegisterState,
    events: (RegisterEvents) -> Unit,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    LaunchedEffect(state) {
        if (state.registerSuccess) {
            Toast.makeText(context,"Successfully Created", Toast.LENGTH_SHORT).show()
            delay(1000L)
            navHostController.popBackStack()
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
            horizontalAlignment = Alignment.CenterHorizontally
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
            Text(
                "Create An Account",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color.Gray,
                    fontSize = 24.sp
                )
            )
            Spacer(
                modifier = modifier.height(24.dp)
            )
            OutlinedTextField(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "name"
                    )
                },
                modifier = modifier.fillMaxWidth(),
                value = state.name.value,
                onValueChange = {
                    events(RegisterEvents.OnNameChange(it))
                },
                label = { Text("Name") },
                isError = state.name.isError,
                supportingText = {
                    Text(state.name.errorMessage ?: "")
                }
            )
            OutlinedTextField(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "phone"
                    )
                },
                modifier = modifier.fillMaxWidth(),
                value = state.phone.value,
                onValueChange = {
                    events(RegisterEvents.OnPhoneChanged(it))
                },
                label = { Text("Phone") },
                isError = state.phone.isError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                supportingText = {
                    Text(state.phone.errorMessage ?: "")
                }
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
                    events(RegisterEvents.OnEmailChange(it))
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
                    events(RegisterEvents.OnPasswordChange(it))
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
            Spacer(
                modifier = modifier.height(12.dp)
            )
            Button(
                shape = RoundedCornerShape(8.dp),
                modifier = modifier.fillMaxSize(),
                onClick = {events.invoke(RegisterEvents.OnRegister)},
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
                            "Registering...",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                } else {
                    Text("Register", style = MaterialTheme.typography.titleMedium)
                }
            }
            TextButton(onClick = {
                navHostController.popBackStack()
            }) {
                Text("Already have an account ? Log in")
            }
        }
    }
}