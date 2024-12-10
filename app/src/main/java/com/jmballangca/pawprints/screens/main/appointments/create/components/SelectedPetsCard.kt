package com.jmballangca.pawprints.screens.main.appointments.create.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jmballangca.pawprints.models.pets.Pet
import com.jmballangca.pawprints.models.pets.getAge
import com.jmballangca.pawprints.utils.RoundedImage
import com.jmballangca.pawprints.utils.Title
import com.jmballangca.pawprints.utils.toPhp


@Composable
fun SelectedPetsCard(
    modifier: Modifier = Modifier,
    pets : List<Pet>,
    isLoading : Boolean,
    onAddPet : () -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth().padding(8.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Pets",
                    style = MaterialTheme.typography.titleMedium
                )
                OutlinedButton(
                    enabled = !isLoading,
                    onClick = onAddPet,
                    modifier = modifier
                        .padding(4.dp)
                        .height(32.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {

                    if (isLoading) {
                        CircularProgressIndicator()
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Add",
                                modifier = modifier.size(16.dp)
                            )
                            Text(
                                text = "Add Pets",
                                fontSize = 12.sp
                            )
                        }
                    }

                }
            }
            pets.map {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RoundedImage(
                        modifier = modifier.size(50.dp),
                        image = it.image ?: ""
                    )
                    Column {
                        Text(
                            "${it.name}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            "${it.birthday?.getAge()}",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }
}