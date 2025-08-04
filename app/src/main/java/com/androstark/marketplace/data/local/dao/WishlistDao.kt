package com.androstark.marketplace.data.local.dao

import androidx.room.*
import com.androstark.marketplace.data.local.entity.WishlistItem
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {
    
    @Query("SELECT * FROM wishlist_items ORDER BY addedAt DESC")
    fun getAllWishlistItems(): Flow<List<WishlistItem>>
    
    @Query("SELECT * FROM wishlist_items WHERE itemId = :itemId")
    suspend fun getWishlistItem(itemId: Int): WishlistItem?
    
    @Query("SELECT EXISTS(SELECT 1 FROM wishlist_items WHERE itemId = :itemId)")
    suspend fun isItemInWishlist(itemId: Int): Boolean
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWishlist(item: WishlistItem)
    
    @Delete
    suspend fun removeFromWishlist(item: WishlistItem)
    
    @Query("DELETE FROM wishlist_items WHERE itemId = :itemId")
    suspend fun removeFromWishlistById(itemId: Int)
    
    @Query("DELETE FROM wishlist_items")
    suspend fun clearWishlist()
}
