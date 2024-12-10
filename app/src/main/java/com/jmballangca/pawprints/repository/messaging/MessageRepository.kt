package com.jmballangca.pawprints.repository.messaging

import com.jmballangca.pawprints.models.message.Message
import com.jmballangca.pawprints.utils.UiState


interface MessageRepository  {
    suspend fun addMessage(
        message: Message,
        result : (UiState<String>) -> Unit
    )

    suspend fun getMessages(userID :String,result: (UiState<List<Message>>) -> Unit)
}