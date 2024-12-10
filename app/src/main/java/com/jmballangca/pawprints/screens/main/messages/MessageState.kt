package com.jmballangca.pawprints.screens.main.messages

import com.jmballangca.pawprints.models.message.Message
import com.jmballangca.pawprints.models.users.Users

data class MessageState(
    val user : Users ?= null,
    val isLoading : Boolean = false,
    val messages : List<Message> = emptyList(),
    val errors : String? = null,
    val isSending : Boolean = false,
    val messageSent : String ? = null,
    val message: String  = ""
)