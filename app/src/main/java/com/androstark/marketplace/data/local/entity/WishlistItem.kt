package com.androstark.marketplace.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist_items")
data class WishlistItem(
    @PrimaryKey
    val itemId: Int,
    val title: String,
    val price: Double,
    val currency: String,
    val imageUrl: String,
    val location: String,
    val category: String,
    val condition: String,
    val addedAt: Long = System.currentTimeMillis()
)
