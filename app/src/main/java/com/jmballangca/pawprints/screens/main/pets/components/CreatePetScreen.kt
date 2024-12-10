package com.jmballangca.pawprints.screens.main.pets.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.jmballangca.pawprints.models.pets.Details
import com.jmballangca.pawprints.models.pets.Pet
import com.jmballangca.pawprints.utils.ImagePicker
import com.jmballangca.pawprints.utils.PawPrintDatePicker
import com.jmballangca.pawprints.utils.Title
import com.jmballangca.pawprints.utils.generateRandomNumberString
import com.jmballangca.pawprints.utils.toast


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePetScreen(
    modifier: Modifier = Modifier,
    isLoading : Boolean,
    userID : String,
    onDismiss : () -> Unit,
    onSave : (Pet,Uri) -> Unit
) {
    var name by remember {
        mutableStateOf("")
    }
    var uri by remember {
        mutableStateOf<Uri?>(null)
    }
    var species by remember {
        mutableStateOf("")
    }
    var breed by remember {
        mutableStateOf("")
    }
    var birthday by remember {
        mutableStateOf("")
    }
    var otherDetails  by remember {
        mutableStateOf<List<Details>>(emptyList())
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { data: Uri? ->
        uri = data
    }
    val context = LocalContext.current
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismiss
    ){
        Surface(modifier = modifier.fillMaxSize()) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                TopAppBar(
                    title = { Text("Create Pet") },
                    navigationIcon = {
                        IconButton(
                            onClick = onDismiss
                        )
                        { Icon(imageVector = Icons.Filled.Close, contentDescription = "Close") }
                    }
                )
                ImagePicker (
                    image = uri,
                    onChangeImage = {  imagePickerLauncher.launch("image/*")}
                )
                // Input for Pet Name
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Pet Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Input for Species
                OutlinedTextField(
                    value = species,
                    onValueChange = { species = it },
                    label = { Text("Species") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Input for Breed
                OutlinedTextField(
                    value = breed,
                    onValueChange = { breed = it },
                    label = { Text("Breed") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                PawPrintDatePicker(
                    label = "Enter birthday",
                    value = birthday,
                    onChange = {
                        birthday = it
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                AddOtherDetailsBottomDialog {
                    otherDetails += it
                }
                otherDetails.forEach {
                    PetDetail(
                        detail = it
                    )
                }


                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    modifier = modifier.fillMaxWidth(),
                    onClick = {
                        val pet : Pet = Pet(
                            id = generateRandomNumberString(15),
                            ownerID =userID,
                            name = name,
                            birthday = birthday,
                            species = species,
                            breed = breed ,
                            otherDetails = otherDetails
                        )
                        if (uri == null) {
                            context.toast("Please add image")
                            return@Button
                        }
                        onSave(pet, uri!!)
                    }
                ) {
                    Text("Create")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOtherDetailsBottomDialog(
    modifier: Modifier = Modifier,
    onSave: (Details) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    // Variables for new key-value pair
    var key by remember { mutableStateOf("") }
    var value by remember { mutableStateOf("") }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Title(title = "Add Details")
                Spacer(modifier = modifier.height(8.dp))

                // Input for Key
                OutlinedTextField(
                    value = key,
                    onValueChange = { key = it },
                    label = { Text("Key") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Input for Value
                OutlinedTextField(
                    value = value,
                    onValueChange = { value = it },
                    label = { Text("Value") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        onSave(
                            Details(
                                label = key,
                                value = value
                            )
                        )
                        key = "" // Clear the input after saving
                        value = ""
                        showBottomSheet = false // Close the bottom sheet
                    }
                ) {
                    Text("Save")
                }
            }
        }
    }

    OutlinedButton(
        shape = MaterialTheme.shapes.small,
        modifier = modifier.fillMaxWidth(),
        onClick = { showBottomSheet = !showBottomSheet }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add"
            )
            Text("Add Details")
        }
    }
}

@Composable
fun PetDetail(
    modifier: Modifier = Modifier,
    detail : Details
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = detail.label?.uppercase() ?: "unknown",
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            text = ":",
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            text = detail.value ?: "unknown",
            style = MaterialTheme.typography.titleSmall.copy(
                color = Color.Gray
            )
        )
    }
}