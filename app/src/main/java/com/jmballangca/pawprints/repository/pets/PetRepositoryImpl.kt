package com.jmballangca.pawprints.repository.pets

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.jmballangca.pawprints.models.pets.Pet
import com.jmballangca.pawprints.utils.UiState

import kotlinx.coroutines.tasks.await

const val PET_COLLECTION = "pets"
class PetRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val storage : FirebaseStorage
): PetRepository {

    override suspend fun createPet(pet: Pet, uri: Uri, result: (UiState<String>) -> Unit) {
        try {
            val storageRef = storage.reference.child("${PET_COLLECTION}/${pet.id}")
            storageRef.putFile(uri).await()
            val imageUrl = storageRef.downloadUrl.await().toString()
            val petWithImage = pet.copy(image = imageUrl)
            firestore.collection(PET_COLLECTION).document(pet.id ?: "").set(petWithImage).await()
            result(UiState.Success("Pet added successfully"))
        } catch (e: Exception) {
            result(UiState.Error(e.message ?: "An error occurred"))
        }
    }

    override suspend fun getAllMyPet(uid: String, result: (UiState<List<Pet>>) -> Unit) {
        result.invoke(UiState.Loading)
        firestore.collection(PET_COLLECTION)
            .whereEqualTo("ownerID", uid)
            .orderBy("createdAt",Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                value?.let {
                    result(UiState.Success(it.toObjects(Pet::class.java)))
                }
                error?.let {
//                    PET_COLLECTION.createErrorLog(
//                        "error fetching pet",
//                        it
//                    )
                    result.invoke(UiState.Error(it.message.toString()))
                }
            }
    }

}