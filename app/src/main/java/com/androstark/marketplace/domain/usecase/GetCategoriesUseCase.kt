package com.androstark.marketplace.domain.usecase

import com.androstark.marketplace.domain.model.Category
import com.androstark.marketplace.domain.repository.MarketplaceRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: MarketplaceRepository
) {
    suspend operator fun invoke(): Result<List<Category>> {
        return repository.getCategories()
    }
}
