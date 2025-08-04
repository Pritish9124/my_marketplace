package com.androstark.marketplace.domain.model

data class ItemDetails(
    val id: Int,
    val title: String,
    val price: Double,
    val currency: String,
    val location: String,
    val timePosted: String,
    val images: List<String>,
    val isFeatured: Boolean,
    val isHot: Boolean,
    val category: String,
    val condition: String,
    val description: String,
    val seller: Seller,
    val specifications: List<Specification>,
    val views: Int,
    val favorites: Int,
    val isNegotiable: Boolean
)

data class Seller(
    val id: Int,
    val name: String,
    val avatar: String,
    val rating: Double,
    val totalReviews: Int,
    val memberSince: String,
    val isVerified: Boolean,
    val responseTime: String
)

data class Specification(
    val key: String,
    val value: String
)
