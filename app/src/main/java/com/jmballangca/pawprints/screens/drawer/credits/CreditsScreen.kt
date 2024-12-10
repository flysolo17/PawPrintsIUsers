package com.jmballangca.pawprints.screens.drawer.credits

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jmballangca.pawprints.R
import com.jmballangca.pawprints.screens.drawer.developer.DEVELOPERS
import com.jmballangca.pawprints.screens.drawer.developer.Developer
import com.jmballangca.pawprints.screens.drawer.developer.DeveloperCard
import com.jmballangca.pawprints.utils.Title

val CREDITS = listOf<Developer>(
    Developer(
        name = "Garcia, Ailen B.",
        profile = R.drawable.ailen_garcia,
        role = "Adviser"
    ),
    Developer(
        name = "Turga, Eleonor",
        profile = R.drawable.eleonor_turga,
        role = "Critic Reader"
    ),
    Developer(
        name = "Sangalang, Sherwin",
        profile = R.drawable.ariel_manuel,
        role = "Data Analyst"
    ),
    Developer(
        name = "Manuel, Ariel R.",
        profile = R.drawable.ariel_manuel,
        role = "Instructor"
    )
)

@Composable
fun CreditScreen(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Image(
                painter = painterResource(R.drawable.paw_logo),
                contentDescription = "Logo",
                modifier = modifier.size(100.dp)
            )
        }
        item {
            Title(title = "Credits")
        }
        items(CREDITS) {
            DeveloperCard(developer = it)
        }
    }
}