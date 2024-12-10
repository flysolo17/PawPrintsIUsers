package com.jmballangca.pawprints.repository.products

import com.jmballangca.pawprints.models.products.Product
import com.jmballangca.pawprints.utils.UiState


interface ProductRepository  {
    suspend fun getAllProducts(
        result : (UiState<List<Product>>) -> Unit
    )
    suspend fun getProductByID(productID : String,result: (UiState<Product?>) -> Unit)

}