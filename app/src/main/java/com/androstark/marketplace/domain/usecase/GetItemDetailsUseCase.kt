package com.androstark.marketplace.domain.usecase

import com.androstark.marketplace.domain.model.ItemDetails
import com.androstark.marketplace.domain.repository.MarketplaceRepository
import javax.inject.Inject

class GetItemDetailsUseCase @Inject constructor(
    private val repository: MarketplaceRepository
) {
    suspend operator fun invoke(itemId: Int): Result<ItemDetails> {
        return try {
            repository.getItemDetails(itemId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
