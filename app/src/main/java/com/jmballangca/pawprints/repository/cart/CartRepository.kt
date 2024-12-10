package com.jmballangca.pawprints.repository.cart

import com.jmballangca.pawprints.models.cart.Cart
import com.jmballangca.pawprints.models.cart.CartWithProduct
import com.jmballangca.pawprints.utils.UiState


interface CartRepository {
    suspend fun addToCart(
        cart: Cart,
        result: (UiState<String>) -> Unit
    )
    suspend fun getAllCart(uid : String,result: (UiState<List<CartWithProduct>>) -> Unit)



    suspend fun removeFromCart(id : String ,result: (UiState<String>) -> Unit)
    suspend fun increaseQuantity(id : String,result: (UiState<String>) -> Unit)
    suspend fun decreaseQuantity(id : String,result: (UiState<String>) -> Unit)
}