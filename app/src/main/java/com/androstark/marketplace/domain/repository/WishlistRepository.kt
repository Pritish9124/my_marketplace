package com.androstark.marketplace.domain.repository

import com.androstark.marketplace.domain.model.ItemDetails
import kotlinx.coroutines.flow.Flow

interface WishlistRepository {
    
    fun getWishlistItems(): Flow<List<ItemDetails>>
    
    suspend fun isItemInWishlist(itemId: Int): Boolean
    
    suspend fun addToWishlist(itemDetails: ItemDetails): Result<Unit>
    
    suspend fun removeFromWishlist(itemId: Int): Result<Unit>
    
    suspend fun toggleWishlist(itemDetails: ItemDetails): Result<Boolean>
    
    suspend fun clearWishlist(): Result<Unit>
}
