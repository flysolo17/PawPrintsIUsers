package com.jmballangca.pawprints.models.cart

import com.jmballangca.pawprints.models.products.Product


data class CartWithProduct(
    val cart : Cart,
    val product: Product,
)