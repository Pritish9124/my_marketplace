package com.androstark.marketplace.data.repository

import com.androstark.marketplace.data.local.dao.WishlistDao
import com.androstark.marketplace.data.local.entity.WishlistItem
import com.androstark.marketplace.domain.model.ItemDetails
import com.androstark.marketplace.domain.repository.WishlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WishlistRepositoryImpl @Inject constructor(
    private val wishlistDao: WishlistDao
) : WishlistRepository {

    override fun getWishlistItems(): Flow<List<ItemDetails>> {
        return wishlistDao.getAllWishlistItems().map { wishlistItems ->
            wishlistItems.map { it.toItemDetails() }
        }
    }

    override suspend fun isItemInWishlist(itemId: Int): Boolean {
        return try {
            wishlistDao.isItemInWishlist(itemId)
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun addToWishlist(itemDetails: ItemDetails): Result<Unit> {
        return try {
            val wishlistItem = itemDetails.toWishlistItem()
            wishlistDao.addToWishlist(wishlistItem)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeFromWishlist(itemId: Int): Result<Unit> {
        return try {
            wishlistDao.removeFromWishlistById(itemId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleWishlist(itemDetails: ItemDetails): Result<Boolean> {
        return try {
            val isInWishlist = wishlistDao.isItemInWishlist(itemDetails.id)
            if (isInWishlist) {
                wishlistDao.removeFromWishlistById(itemDetails.id)
                Result.success(false)
            } else {
                val wishlistItem = itemDetails.toWishlistItem()
                wishlistDao.addToWishlist(wishlistItem)
                Result.success(true)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun clearWishlist(): Result<Unit> {
        return try {
            wishlistDao.clearWishlist()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Extension functions for mapping between domain and data models
    private fun WishlistItem.toItemDetails(): ItemDetails {
        return ItemDetails(
            id = itemId,
            title = title,
            price = price,
            currency = currency,
            location = location,
            timePosted = "Saved",
            images = listOf(imageUrl),
            isFeatured = false,
            isHot = false,
            category = category,
            condition = condition,
            description = "Saved item from wishlist",
            seller = com.androstark.marketplace.domain.model.Seller(
                id = 0,
                name = "Unknown",
                avatar = "",
                rating = 0.0,
                totalReviews = 0,
                memberSince = "",
                isVerified = false,
                responseTime = ""
            ),
            specifications = emptyList(),
            views = 0,
            favorites = 0,
            isNegotiable = false
        )
    }

    private fun ItemDetails.toWishlistItem(): WishlistItem {
        return WishlistItem(
            itemId = id,
            title = title,
            price = price,
            currency = currency,
            imageUrl = images.firstOrNull() ?: "",
            location = location,
            category = category,
            condition = condition
        )
    }
}
