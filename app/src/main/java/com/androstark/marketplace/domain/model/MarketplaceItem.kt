package com.androstark.marketplace.domain.model

data class MarketplaceItem(
    val id: Int,
    val title: String,
    val price: Double,
    val currency: String,
    val location: String,
    val timePosted: String,
    val imageUrl: String,
    val isFeatured: Boolean,
    val isHot: Boolean,
    val category: String,
    val condition: String,
    val description: String
)
