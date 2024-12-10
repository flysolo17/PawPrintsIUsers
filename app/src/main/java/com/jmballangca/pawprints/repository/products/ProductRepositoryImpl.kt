package com.jmballangca.pawprints.repository.products

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.jmballangca.pawprints.models.products.Product
import com.jmballangca.pawprints.utils.CreateLog
import com.jmballangca.pawprints.utils.UiState
import kotlinx.coroutines.delay

const val PRODUCTS_COLLECTION = "products";
class ProductRepositoryImpl(
    private val firestore: FirebaseFirestore
): ProductRepository {
    override suspend fun getAllProducts(result: (UiState<List<Product>>) -> Unit) {
        result(UiState.Loading)
        delay(1000)
        firestore.collection(PRODUCTS_COLLECTION)
            .whereEqualTo(
                "visibility",
                true
            ).orderBy(
                "createdAt",
                Query.Direction.DESCENDING
            ).addSnapshotListener { value, error ->
                value?.let {
                    result(UiState.Success(it.toObjects(Product::class.java)))
                }
                error?.let {
                    result(UiState.Error(it.message.toString()))
                }
            }
    }

    override suspend fun getProductByID(productID: String, result: (UiState<Product?>) -> Unit) {
        result(UiState.Loading)
        firestore.collection(PRODUCTS_COLLECTION)
            .document(productID)
            .get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val data = it.toObject(Product::class.java)
                    PRODUCTS_COLLECTION.CreateLog<Product?>(data)
                    result(UiState.Success(data))
                } else {
                    PRODUCTS_COLLECTION.CreateLog("No product found!")
                    result(UiState.Error("No product found!"))
                }
            }.addOnFailureListener {
                PRODUCTS_COLLECTION.CreateLog(it.message.toString())
                result(UiState.Error(it.message.toString()))
            }
    }
}