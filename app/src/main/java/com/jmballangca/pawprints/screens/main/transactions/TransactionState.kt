package com.jmballangca.pawprints.screens.main.transactions

import com.jmballangca.pawprints.models.transaction.Transaction
import com.jmballangca.pawprints.models.users.Users


data class TransactionState(
    val isLoading : Boolean = false,
    val transactions : List<Transaction> = emptyList() ,
    val errors : String ? = null,
    val user : Users ? = null,
)