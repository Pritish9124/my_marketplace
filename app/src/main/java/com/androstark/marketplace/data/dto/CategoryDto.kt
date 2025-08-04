package com.androstark.marketplace.data.dto

import com.androstark.marketplace.domain.model.Category
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    val id: Int,
    val name: String,
    val icon: String,
    val color: String,
    val adCount: Int
)

fun CategoryDto.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        icon = icon,
        color = color,
        adCount = adCount
    )
}
