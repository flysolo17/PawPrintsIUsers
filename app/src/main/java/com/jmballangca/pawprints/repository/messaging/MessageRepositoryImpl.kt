package com.jmballangca.pawprints.repository.messaging

import android.util.Log
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jmballangca.pawprints.models.message.Message
import com.jmballangca.pawprints.utils.UiState
import com.jmballangca.pawprints.utils.generateRandomNumberString
import kotlinx.coroutines.delay

class MessageRepositoryImpl(
    private val firestore: FirebaseFirestore
): MessageRepository {
    private val messageCollection = firestore.collection("messages")
    override suspend fun addMessage(message: Message, result: (UiState<String>) -> Unit) {
        result.invoke(UiState.Loading)
        delay(1000)
        messageCollection.document(message.id!!)
            .set(message)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result(UiState.Success("Message sent!"))
                } else {
                    result(UiState.Error("Error sending message!"))
                }
            }
    }


    override suspend fun getMessages(userID: String, result: (UiState<List<Message>>) -> Unit) {
        result.invoke(UiState.Loading)
        delay(1000)
        messageCollection.
                where(
                    Filter.or(
                        Filter.equalTo("sender", userID),
                        Filter.equalTo("receiver", userID)
                    )
                ).orderBy("createdAt",Query.Direction.DESCENDING)
                    .addSnapshotListener { value, error ->
                        value?.let {
                            Log.e("messages","error",error)
                        result(UiState.Success(it.toObjects(Message::class.java)))
                    }
            error?.let {

                Log.e("messages","error",it)
                result.invoke(UiState.Error("Error getting messages"))
            }
        }
    }
}