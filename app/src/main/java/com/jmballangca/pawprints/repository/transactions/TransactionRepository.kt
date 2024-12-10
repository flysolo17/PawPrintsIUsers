package com.jmballangca.pawprints.repository.transactions

import com.jmballangca.pawprints.models.transaction.Transaction
import com.jmballangca.pawprints.models.transaction.TransactionStatus
import com.jmballangca.pawprints.utils.UiState


interface TransactionRepository  {
    suspend fun createTransaction(transaction : Transaction ,result : (UiState<String>) -> Unit)
    suspend fun getALlMyTransactions(userID : String,result: (UiState<List<Transaction>>) -> Unit)
}