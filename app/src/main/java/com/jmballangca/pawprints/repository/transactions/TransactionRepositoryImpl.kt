package com.jmballangca.pawprints.repository.transactions

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jmballangca.pawprints.models.appointment.Inbox
import com.jmballangca.pawprints.models.appointment.InboxTpe
import com.jmballangca.pawprints.models.transaction.Transaction
import com.jmballangca.pawprints.models.transaction.createMessage
import com.jmballangca.pawprints.repository.inbox.INBOX_COLLECTION
import com.jmballangca.pawprints.utils.UiState
import com.jmballangca.pawprints.utils.generateRandomNumberString
import kotlinx.coroutines.delay
import java.util.Date

const val TRANSACTION_COLLECTION = "transactions"
class TransactionRepositoryImpl(
    private val firestore : FirebaseFirestore
): TransactionRepository {
    override suspend fun createTransaction(
        transaction: Transaction,
        result: (UiState<String>) -> Unit
    ) {
        val batch = firestore.batch()
        val inbox = Inbox(
            userID = transaction.userID,
            id = generateRandomNumberString(),
            message = transaction.status.createMessage(),
            collectionID = transaction.id,
            type = InboxTpe.TRANSACTIONS,
            createdAt = Date()
        )

        try {
            result(UiState.Loading)

            val inboxRef = firestore.collection(INBOX_COLLECTION).document(inbox.id!!)
            batch.set(inboxRef, inbox)

            val transactionRef = firestore.collection(TRANSACTION_COLLECTION).document(transaction.id!!)
            batch.set(transactionRef, transaction)
            batch.commit()
                .addOnSuccessListener {
                    result(UiState.Success("Successfully Created!"))
                }
                .addOnFailureListener { exception ->
                    result(UiState.Error("Failed to create transaction: ${exception.message}"))
                }
        } catch (e: Exception) {
            result(UiState.Error("Unexpected error: ${e.message}"))
        }
    }


    override suspend fun getALlMyTransactions(
        userID: String,
        result: (UiState<List<Transaction>>) -> Unit
    ) {
        result.invoke(UiState.Loading)
        delay(1000)
        firestore.collection(TRANSACTION_COLLECTION)
            .whereEqualTo("userID",userID)
            .orderBy("createdAt",Query.Direction.DESCENDING)
            .orderBy("updatedAt",Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                value?.let {
                    result(UiState.Success(it.toObjects(Transaction::class.java)))
                }
                error?.let {
                    result(UiState.Error(it.message.toString()))
                }
            }
    }
}