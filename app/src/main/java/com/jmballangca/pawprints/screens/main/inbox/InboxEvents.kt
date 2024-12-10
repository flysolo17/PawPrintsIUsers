package com.jmballangca.pawprints.screens.main.inbox

import com.jmballangca.pawprints.models.users.Users


sealed interface InboxEvents {
    data class OnSetUsers(val users: Users) : InboxEvents
    data class OnGetMyInbox(val uid : String) : InboxEvents
}