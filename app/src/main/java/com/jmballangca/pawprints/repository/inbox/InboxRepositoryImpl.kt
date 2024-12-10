package com.jmballangca.pawprints.repository.inbox

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jmballangca.pawprints.models.appointment.Inbox
import com.jmballangca.pawprints.utils.UiState
import kotlinx.coroutines.delay

const val INBOX_COLLECTION = "inbox"
class InboxRepositoryImpl(private val firestore: FirebaseFirestore): InboxRepository {
    override suspend fun getAllInboxByUserID(
        userID: String,
        result: (UiState<List<Inbox>>) -> Unit
    ) {
        result.invoke(UiState.Loading)
        delay(1000)
        firestore.collection(INBOX_COLLECTION)
            .whereEqualTo("userID",userID)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                Log.e(
                    INBOX_COLLECTION,
                    "Error",
                    error
                )
                value?.let {
                    result.invoke(UiState.Success(it.toObjects(Inbox::class.java)))
                }
                error?.let {
                    result.invoke(UiState.Error(it.message.toString()))
                }
            }
    }
}