package com.jmballangca.pawprints.screens.main.messages

import com.jmballangca.pawprints.models.message.Message
import com.jmballangca.pawprints.models.users.Users


sealed interface MessageEvents  {
    data class OnSetUsers(val user : Users) : MessageEvents
    data class OnGetMyMessage(val userID : String) : MessageEvents
    data class SendMessage(val message : String) : MessageEvents
    data class OnMessageChange(val message: String) : MessageEvents
}