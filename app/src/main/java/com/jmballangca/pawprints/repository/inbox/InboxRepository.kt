package com.jmballangca.pawprints.repository.inbox

import com.jmballangca.pawprints.models.appointment.Inbox
import com.jmballangca.pawprints.utils.UiState


interface InboxRepository  {
    suspend fun getAllInboxByUserID(userID : String,result : (UiState<List<Inbox>>) -> Unit)
}