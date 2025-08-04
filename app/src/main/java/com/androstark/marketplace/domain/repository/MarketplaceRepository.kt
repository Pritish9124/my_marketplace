package com.androstark.marketplace.domain.repository

import com.androstark.marketplace.domain.model.Category
import com.androstark.marketplace.domain.model.ItemDetails
import com.androstark.marketplace.domain.model.MarketplaceItem

interface MarketplaceRepository {
    suspend fun getCategories(): Result<List<Category>>
    suspend fun getFeaturedItems(): Result<List<MarketplaceItem>>
    suspend fun getItemDetails(itemId: Int): Result<ItemDetails>
    suspend fun searchItems(query: String): Result<List<MarketplaceItem>>
    suspend fun getItemsByCategory(categoryId: Int): Result<List<MarketplaceItem>>
}
