package com.jmballangca.pawprints.screens.main.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jmballangca.pawprints.models.message.Message
import com.jmballangca.pawprints.screens.main.transactions.TransactionEvents
import com.jmballangca.pawprints.ui.theme.PawPrintsTheme
import com.jmballangca.pawprints.utils.toast
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun MessageScreen(
    modifier: Modifier = Modifier,
    state: MessageState,
    events: (MessageEvents) -> Unit,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    LaunchedEffect(state.user?.id) {
        state.user?.id?.let {
            events(MessageEvents.OnGetMyMessage(it))
        }
    }

    LaunchedEffect(state) {

        if (state.errors != null) {
            context.toast(state.errors)
        }
        if (state.messageSent != null) {
            context.toast(state.messageSent)
        }
    }
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        if(state.isLoading) {
            LinearProgressIndicator(
                modifier = modifier.fillMaxWidth()
            )
        }
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .weight(1f),
            reverseLayout = true
        ) {

            items(state.messages) {
                MessageCard(
                    it=it,
                    mine = it.sender == state.user?.id
                )
            }
        }
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            TextField(
                value = state.message,
                onValueChange = {events(MessageEvents.OnMessageChange(it))},
                modifier = modifier.fillMaxWidth().padding(8.dp),
                shape = MaterialTheme.shapes.large,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                placeholder = { Text("Enter your message") },
                trailingIcon = {
                    IconButton(
                        enabled = !state.isSending,
                        onClick = {
                            if (state.message.isEmpty()) {
                                context.toast("Please add message")
                                return@IconButton
                            }
                            events.invoke(MessageEvents.SendMessage(state.message))
                        }
                    ) {
                        if (state.isSending) {
                            CircularProgressIndicator(
                                modifier = modifier.size(18.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Send"
                            )
                        }

                    }
                }
            )
        }
    }
}

@Composable
fun MessageCard(
    modifier: Modifier = Modifier,
    it: Message,
    mine : Boolean
) {
    Box(
        modifier = modifier.fillMaxWidth().padding(8.dp),
        contentAlignment = if (mine) Alignment.CenterEnd else Alignment.CenterStart
    ){
        Column(
            modifier = modifier
                .fillMaxWidth(
                    0.5f
                )
                .background(
                    shape = MaterialTheme.shapes.small,
                    color = if (mine) MaterialTheme.colorScheme.primary else Color.Gray
                )
                .padding(8.dp),
        ) {
            Text(
                "${it.message}",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleSmall.copy(
                    color = if(mine) MaterialTheme.colorScheme.onPrimary else Color.Black
                )

            )
            val dateFormat = SimpleDateFormat("MMMM dd hh:mm aa", Locale.getDefault())
            val formattedDate = dateFormat.format(it.createdAt)

            Text(
                text = formattedDate,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = if(mine) Color.Gray else Color.Black
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MessagePrev() {
    PawPrintsTheme {
        MessageScreen(
            state = MessageState(),
            events = {},
            navHostController = rememberNavController()
        )
    }
}