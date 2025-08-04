package com.androstark.marketplace.domain.usecase

import com.androstark.marketplace.domain.model.MarketplaceItem
import com.androstark.marketplace.domain.repository.MarketplaceRepository
import javax.inject.Inject

class GetFeaturedItemsUseCase @Inject constructor(
    private val repository: MarketplaceRepository
) {
    suspend operator fun invoke(): Result<List<MarketplaceItem>> {
        return repository.getFeaturedItems()
    }
}
