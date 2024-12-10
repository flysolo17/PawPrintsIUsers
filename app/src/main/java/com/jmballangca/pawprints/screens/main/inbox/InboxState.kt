package com.jmballangca.pawprints.screens.main.inbox

import com.jmballangca.pawprints.models.appointment.Inbox
import com.jmballangca.pawprints.models.users.Users


data class InboxState(
    val user : Users ? = null,
    val isLoading : Boolean = false,
    val inboxes : List<Inbox> = emptyList(),
    val errors : String? = null
)