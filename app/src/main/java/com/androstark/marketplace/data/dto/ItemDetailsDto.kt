package com.androstark.marketplace.data.dto

import com.androstark.marketplace.domain.model.ItemDetails
import com.androstark.marketplace.domain.model.Seller
import com.androstark.marketplace.domain.model.Specification
import com.google.gson.annotations.SerializedName

data class ItemDetailsDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("timePosted")
    val timePosted: String,
    @SerializedName("images")
    val images: List<String>,
    @SerializedName("isFeatured")
    val isFeatured: Boolean,
    @SerializedName("isHot")
    val isHot: Boolean,
    @SerializedName("category")
    val category: String,
    @SerializedName("condition")
    val condition: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("seller")
    val seller: SellerDto,
    @SerializedName("specifications")
    val specifications: List<SpecificationDto>,
    @SerializedName("views")
    val views: Int,
    @SerializedName("favorites")
    val favorites: Int,
    @SerializedName("isNegotiable")
    val isNegotiable: Boolean
)

data class SellerDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("rating")
    val rating: Double,
    @SerializedName("totalReviews")
    val totalReviews: Int,
    @SerializedName("memberSince")
    val memberSince: String,
    @SerializedName("isVerified")
    val isVerified: Boolean,
    @SerializedName("responseTime")
    val responseTime: String
)

data class SpecificationDto(
    @SerializedName("key")
    val key: String,
    @SerializedName("value")
    val value: String
)

fun ItemDetailsDto.toDomain(): ItemDetails {
    return ItemDetails(
        id = id,
        title = title,
        price = price,
        currency = currency,
        location = location,
        timePosted = timePosted,
        images = images,
        isFeatured = isFeatured,
        isHot = isHot,
        category = category,
        condition = condition,
        description = description,
        seller = seller.toDomain(),
        specifications = specifications.map { it.toDomain() },
        views = views,
        favorites = favorites,
        isNegotiable = isNegotiable
    )
}

fun SellerDto.toDomain(): Seller {
    return Seller(
        id = id,
        name = name,
        avatar = avatar,
        rating = rating,
        totalReviews = totalReviews,
        memberSince = memberSince,
        isVerified = isVerified,
        responseTime = responseTime
    )
}

fun SpecificationDto.toDomain(): Specification {
    return Specification(
        key = key,
        value = value
    )
}
