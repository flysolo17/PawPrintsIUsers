package com.jmballangca.pawprints.screens.main.pets.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jmballangca.pawprints.models.pets.Pet
import com.jmballangca.pawprints.models.pets.getAge
import com.jmballangca.pawprints.utils.RoundedImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectPetBottomDialog(
    modifier: Modifier = Modifier,
    pets: List<Pet>,
    onConfirm: (List<Pet>) -> Unit,
    sheetState: SheetState,
    onDismiss: () -> Unit
) {
    var selectedPets by remember { mutableStateOf(listOf<Pet>()) }
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier
    ) {
        LazyColumn(
            modifier = modifier.wrapContentSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "Select Pets",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            itemsIndexed(pets) { index ,pet->
                val isSelected = selectedPets.any {
                    it.id == pet.id
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isSelected,
                        onCheckedChange = {
                            if (it) {
                               selectedPets =  selectedPets + pet
                            } else {
                              val current =selectedPets.toMutableList()
                                current.removeAt(index)
                                selectedPets = current
                            }
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    RoundedImage(
                        image = pet.image ?: "",
                        modifier = modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = pet.name ?: "Unknown", fontWeight = FontWeight.Medium)
                        Text(
                            text = pet.birthday?.getAge() ?: "Age Unknown",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }
            item {
                Button(
                    onClick = {
                        onConfirm(selectedPets.toList())
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text(text = "Confirm Selection")
                }
            }
        }



    }
}