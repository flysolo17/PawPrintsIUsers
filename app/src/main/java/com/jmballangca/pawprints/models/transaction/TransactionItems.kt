package com.jmballangca.pawprints.models.transaction

import android.os.Parcelable
import com.jmballangca.pawprints.models.cart.CartWithProduct
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionItems(
    val productID : String ? = null,
    val name : String ? = null,
    val imageUrl : String ? = null,
    val quantity : Int ? = null,
    val price : Double ? = null,
) : Parcelable

fun List<CartWithProduct>.toTransactionItems() : List<TransactionItems> {
    val items = mutableListOf<TransactionItems>()
    this.forEach {
        items.add(
            TransactionItems(
                productID = it.product.id,
                name = it.product.name,
                imageUrl = it.product.image,
                quantity =  it.cart.quantity,
                price = it.product.price
            )
        )
    }
    return items;
}
