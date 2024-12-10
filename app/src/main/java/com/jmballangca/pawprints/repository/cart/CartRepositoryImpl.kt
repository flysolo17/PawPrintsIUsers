package com.jmballangca.pawprints.repository.cart

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.jmballangca.pawprints.models.cart.Cart
import com.jmballangca.pawprints.models.cart.CartWithProduct
import com.jmballangca.pawprints.models.products.Product
import com.jmballangca.pawprints.repository.products.PRODUCTS_COLLECTION
import com.jmballangca.pawprints.utils.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class CartRepositoryImpl(
    private val firestore: FirebaseFirestore,

): CartRepository {
    override suspend fun addToCart(cart: Cart, result: (UiState<String>) -> Unit) {
        result(UiState.Loading)
        try {
            // Query Firestore to check if the cart item already exists
            val querySnapshot = firestore.collection("carts")
                .whereEqualTo("userID", cart.userID)
                .whereEqualTo("productID", cart.productID)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                // If the item is already in the cart, increment the quantity
                val existingCartDoc = querySnapshot.documents.first()
                val existingCart = existingCartDoc.toObject(Cart::class.java)
                val newQuantity = existingCart?.quantity?.plus(1) ?: 1

                firestore.collection("carts")
                    .document(existingCartDoc.id)
                    .update("quantity", newQuantity)
                    .await()

                result(UiState.Success("Cart updated successfully"))
            } else {
                // Add a new cart item if it does not exist
                firestore.collection("carts")
                    .document(cart.id!!)
                    .set(cart)
                    .await()
                delay(1000)
                result(UiState.Success("Item added to cart successfully"))
            }
        } catch (e: Exception) {
            result(UiState.Error(e.localizedMessage ?: "Error adding to cart"))
        }
    }

    override suspend fun getAllCart(uid: String, result: (UiState<List<CartWithProduct>>) -> Unit) {
        result(UiState.Loading)

        try {
            firestore.collection("carts")
                .whereEqualTo("userID", uid)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        result(UiState.Error(error.localizedMessage ?: "Error listening to cart updates"))
                        return@addSnapshotListener
                    }

                    if (snapshot != null && !snapshot.isEmpty) {
                        val cartItems = snapshot.documents.map { doc ->
                            doc.toObject(Cart::class.java)!!
                        }

                        // Launch a coroutine for async operations
                        CoroutineScope(Dispatchers.IO).launch {
                            try {

                                val cartWithProducts = cartItems.map { cart ->
                                    val product = firestore.collection(PRODUCTS_COLLECTION)
                                        .document(cart.productID!!)
                                        .get()
                                        .await()
                                        .toObject(Product::class.java)
                                    if (product != null) {
                                        CartWithProduct(cart = cart, product = product)
                                    } else {
                                        null
                                    }
                                }
                                withContext(Dispatchers.Main) {
                                    result(UiState.Success(cartWithProducts.filterNotNull()))
                                }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    result(UiState.Error(e.localizedMessage ?: "Error retrieving product details"))
                                }
                            }
                        }
                    } else {
                        result(UiState.Success(emptyList()))
                    }
                }
        } catch (e: Exception) {
            result(UiState.Error(e.localizedMessage ?: "Error retrieving cart items"))
        }
    }



    override suspend fun removeFromCart(id: String, result: (UiState<String>) -> Unit) {
        result(UiState.Loading)
        try {
            firestore.collection("carts").document(id).delete().await()
            result(UiState.Success("Item removed from cart"))
        } catch (e: Exception) {
            result(UiState.Error(e.localizedMessage ?: "Error removing item from cart"))
        }
    }

    override suspend fun increaseQuantity(id: String, result: (UiState<String>) -> Unit) {
        result(UiState.Loading)
        try {
            firestore.collection("carts")
                .document(id)
                .update("quantity", FieldValue.increment(1))
                .await()

            result(UiState.Success("Quantity increased"))
        } catch (e: Exception) {
            result(UiState.Error(e.localizedMessage ?: "Error increasing quantity"))
        }
    }

    override suspend fun decreaseQuantity(id: String, result: (UiState<String>) -> Unit) {
        result(UiState.Loading)
        try {
            firestore.collection("carts")
                .document(id)
                .update("quantity", FieldValue.increment(-1))
                .await()
            result(UiState.Success("Quantity decreased"))
        } catch (e: Exception) {
            result(UiState.Error(e.localizedMessage ?: "Error decreasing quantity"))
        }
    }
}
