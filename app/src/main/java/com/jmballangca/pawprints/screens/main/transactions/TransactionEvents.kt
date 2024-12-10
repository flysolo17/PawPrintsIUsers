package com.jmballangca.pawprints.screens.main.transactions

import com.jmballangca.pawprints.models.transaction.Transaction
import com.jmballangca.pawprints.models.users.Users


sealed interface TransactionEvents {
    data class OnSetUsers(val users: Users) : TransactionEvents
    data class OnGetMyTransactions(val userID : String) : TransactionEvents
}