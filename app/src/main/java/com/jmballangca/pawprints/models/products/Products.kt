package com.jmballangca.pawprints.models.products

import android.os.Parcelable
import com.jmballangca.pawprints.models.transaction.TransactionItems
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Product(
    var id: String? = null,
    var name: String? = null,
    var image: String? = null,
    var type: ProductType? = null,
    var description: String? = null,
    var categoryID: String? = null,
    var features: String? = null,
    var contents: String? = null,
    var quantity: Int? = null,
    var visibility: Boolean? = null,
    var cost: Double? = null,
    var price: Double? = null,
    var discount: Discount? = null,
    var expiration: Date? = null,
    var createdAt: Date = Date(),
    var updatedAt: Date = Date(),
    var movement: Movement? = null,
    ) : Parcelable

enum class ProductType {
    GOODS,
    SERVICES
}

enum class Movement {
    IN,
    OUT,
    SOLD
}

fun Product.toItem() : TransactionItems {
    return  TransactionItems(
        productID = this.id,
        name = this.name,
        imageUrl = this.image,
        quantity = 1,
        price = this.price
    )
}

fun List<Product>.getNames(): String {
    return this.joinToString(separator = ",") { it.name ?: "" }
}
