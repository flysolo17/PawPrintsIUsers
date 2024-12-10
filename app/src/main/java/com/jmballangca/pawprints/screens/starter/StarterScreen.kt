package com.jmballangca.pawprints.screens.starter

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jmballangca.pawprints.R
import com.jmballangca.pawprints.router.AppRouter

@Composable
fun StarterScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    Scaffold {
        Column(
            modifier = modifier.fillMaxSize().padding(it).padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.paw_logo),
                contentDescription = "Logo",
                modifier =  modifier.size(150.dp)
            )
            Spacer(
                modifier = modifier.height(24.dp)
            )
            Text(
                "PawPrints",
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
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF001B44),
                    contentColor = Color.White
                ),
                onClick = {
                    navHostController.navigate(AppRouter.LoginScreen.route)
                }
            ) {
                Row(
                    modifier = modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Pets,
                        contentDescription = "Pets"
                    )
                    Text("Tap To Start", style = MaterialTheme.typography.titleMedium)
                }

            }
        }
    }
}