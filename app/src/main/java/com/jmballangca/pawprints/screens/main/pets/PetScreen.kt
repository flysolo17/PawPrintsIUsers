package com.jmballangca.pawprints.screens.main.pets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jmballangca.pawprints.screens.main.pets.components.CreatePetScreen
import com.jmballangca.pawprints.screens.main.pets.components.PetCard
import com.jmballangca.pawprints.utils.ProgressBar
import com.jmballangca.pawprints.utils.Title
import com.jmballangca.pawprints.utils.toast
import kotlinx.coroutines.delay


@Composable
fun PetScreen(
    modifier: Modifier = Modifier,
    userID : String,
    state: PetState,
    events: (PetEvents) -> Unit,
    navHostController: NavHostController
) {
    val context = LocalContext.current

    var createPetDialog by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(userID) {
        if (userID.isNotEmpty()) {
            events(PetEvents.OnGetPets(userID))
        }
    }
    LaunchedEffect(state){
        if (state.errors != null) {
            context.toast(state.errors)
        }
        if (state.petCreated != null) {
            context.toast(state.petCreated)
            createPetDialog = !createPetDialog
        }
    }

    if (createPetDialog) {
        CreatePetScreen(
            userID  = userID,
            onDismiss = {
                createPetDialog = !createPetDialog
            },
            isLoading = state.creatingPet,
            onSave = { pet ,uri ->
                events.invoke(PetEvents.OnCreatePet(pet,uri))

            }
        )
    }
    when {
        state.isLoading -> ProgressBar(
            title = "Getting all pets.."
        )
        else -> {
            LazyVerticalGrid(
                modifier = modifier.fillMaxSize().padding(8.dp),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                item(
                    span = { GridItemSpan(2) }
                ) {
                    PetHeader(
                        onCreatePet  = {
                            createPetDialog = !createPetDialog
                        }
                    )
                }
                items(state.pets) {
                    PetCard(pet = it, onClick = {})
                }
            }
        }
    }
}

@Composable
fun PetHeader(modifier:  Modifier = Modifier, onCreatePet: () -> Unit) {
    Row(
        modifier = modifier.fillMaxWidth().padding(
            bottom = 16.dp
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Title(title = "My Pets")
        OutlinedButton(onClick = onCreatePet) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add"
                )
                Text("New")
            }
        }
    }
}
