package com.jmballangca.pawprints.models.products

import java.util.Date

data class Category(
    var id: String? = null,
    var name: String? = null,
    var createdAt: Date? = Date()
)
