package com.androstark.marketplace.data.dto

import com.androstark.marketplace.domain.model.MarketplaceItem
import kotlinx.serialization.Serializable

@Serializable
data class MarketplaceItemDto(
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

fun MarketplaceItemDto.toDomain(): MarketplaceItem {
    return MarketplaceItem(
        id = id,
        title = title,
        price = price,
        currency = currency,
        location = location,
        timePosted = timePosted,
        imageUrl = imageUrl,
        isFeatured = isFeatured,
        isHot = isHot,
        category = category,
        condition = condition,
        description = description
    )
}
