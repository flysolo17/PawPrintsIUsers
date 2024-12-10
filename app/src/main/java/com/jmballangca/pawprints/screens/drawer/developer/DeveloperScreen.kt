package com.jmballangca.pawprints.screens.drawer.developer

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jmballangca.pawprints.R
import com.jmballangca.pawprints.utils.Description
import com.jmballangca.pawprints.utils.ProfileImage
import com.jmballangca.pawprints.utils.Title

data class Developer(
    val name : String,
    @DrawableRes val profile : Int,
    val role : String
)

 val DEVELOPERS = listOf<Developer>(
    Developer(
        name = "Lumogda, Katherine V.",
        profile = R.drawable.dev1,
        role = "Developer"
    ),
     Developer(
         name = "Lacsao, Khristine M.",
         profile = R.drawable.dev2,
         role = "Documentator"
     ),
     Developer(
         name = "Cuello, Eugene J.",
         profile = R.drawable.dev3,
         role = "Researcher"
     ),
     Developer(
         name = "Rea, Jocel Mae D.",
         profile = R.drawable.dev4,
         role = "Designer"
     ),
)

@Composable
fun DeveloperScreen(
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
            Title(title = "Developers")
        }
        items(DEVELOPERS) {
            DeveloperCard(developer = it)
        }
    }
}

@Composable
fun DeveloperCard(
    modifier: Modifier = Modifier,
    developer : Developer
) {
    Card(
        modifier = modifier.fillMaxWidth().padding(8.dp),
        elevation = CardDefaults.cardElevation(
            4.dp
        )
    ) {
        Row(
            modifier = modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(developer.profile),
                contentDescription = developer.name,
                contentScale = ContentScale.Crop,
                modifier = modifier.size(80.dp).clip(CircleShape)
            )
            Column {
                Text(
                    developer.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Description(
                    description = developer.role
                )
            }

        }
    }
}