package com.jmballangca.pawprints.screens.main.inbox

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jmballangca.pawprints.models.appointment.Inbox
import com.jmballangca.pawprints.models.appointment.InboxTpe
import com.jmballangca.pawprints.ui.theme.PawPrintsTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InboxScreen(
    modifier: Modifier = Modifier,
    state: InboxState,
    events: (InboxEvents) -> Unit,
    navHostController: NavHostController
) {
    LaunchedEffect(state.user?.id) {
            state.user?.id?.let {
                events(InboxEvents.OnGetMyInbox(it))
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = { Text("Inbox") },
                navigationIcon = { IconButton(onClick = {navHostController.popBackStack()}) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                }
            )
        }
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
        ) {
            if (state.isLoading) {
                item {
                    LinearProgressIndicator(
                        modifier = modifier.fillMaxWidth()
                    )
                }
            }
            items(state.inboxes) {
                InboxCard(
                    inbox = it
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun InboxCard(
    modifier: Modifier = Modifier,
    inbox : Inbox
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        ListItem(
            modifier = modifier.padding(8.dp),
            text = {
                Column {
                    Text(
                        inbox.type.name,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.Gray
                        )
                    )
                    Text(
                        "${inbox.message}",
                        style = MaterialTheme.typography.titleSmall
                    )
                    FormattedDateText(inbox.createdAt)
                }

            }
        )
    }
}


@Composable
fun FormattedDateText(createdAt: Date) {
    val formattedDate = try {
        // Format the Date object to "MM/dd/yyyy hh:mm aa"
        val dateFormat = SimpleDateFormat("MM/dd/yyyy hh:mm aa", Locale.getDefault())
        dateFormat.format(createdAt)
    } catch (e: Exception) {
        "Invalid Date"
    }

    Text(
        formattedDate,
        style = MaterialTheme.typography.labelSmall.copy(
            color = Color.Gray,
            fontWeight = FontWeight.Bold
        )
    )
}


@Preview
@Composable
private fun InboxCardPrev(

) {
    PawPrintsTheme {
        InboxCard(
            inbox =  Inbox(
                id = "adfdssdfsd",
                type = InboxTpe.APPOINTMENTS,
                message = "Youre appointment has been confirmed",
                createdAt = Date()
            )
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun InboxScreenPrev() {
    PawPrintsTheme {
        InboxScreen(
            state = InboxState(),
            events = {},
            navHostController = rememberNavController()
        )
    }

}