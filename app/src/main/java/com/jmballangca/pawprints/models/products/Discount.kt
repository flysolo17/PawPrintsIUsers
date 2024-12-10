package com.jmballangca.pawprints.models.products

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Discount(
    var value: Double,
    var expiration: Date? = null
) : Parcelable
