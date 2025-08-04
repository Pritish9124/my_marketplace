package com.androstark.marketplace.domain.usecase

import com.androstark.marketplace.domain.model.ItemDetails
import com.androstark.marketplace.domain.model.SellFormState
import com.androstark.marketplace.domain.model.Seller
import com.androstark.marketplace.domain.model.Specification
import com.androstark.marketplace.domain.repository.MarketplaceRepository
import javax.inject.Inject
import kotlin.random.Random

class SubmitAdUseCase @Inject constructor(
    private val marketplaceRepository: MarketplaceRepository
) {
    
    suspend operator fun invoke(formState: SellFormState): Result<ItemDetails> {
        return try {
            val newAd = createItemDetailsFromForm(formState)
            
            // In a real app, this would call an API to create the ad
            // For now, we'll simulate the creation and return the new ad
            Result.success(newAd)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun createItemDetailsFromForm(formState: SellFormState): ItemDetails {
        val newId = Random.nextInt(10000, 99999) // Generate random ID for demo
        
        return ItemDetails(
            id = newId,
            title = formState.title,
            price = formState.price.toDouble(),
            currency = "₹",
            location = formState.location?.address ?: "",
            timePosted = "Just now",
            images = formState.images,
            isFeatured = false,
            isHot = false,
            category = formState.category,
            condition = formState.condition,
            description = formState.description,
            seller = createSellerFromContactInfo(formState.contactInfo),
            specifications = createSpecificationsFromForm(formState),
            views = 0,
            favorites = 0,
            isNegotiable = formState.isNegotiable
        )
    }
    
    private fun createSellerFromContactInfo(contactInfo: com.androstark.marketplace.domain.model.ContactInfo): Seller {
        return Seller(
            id = Random.nextInt(1000, 9999),
            name = contactInfo.name,
            avatar = "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=100", // Default avatar
            rating = 0.0, // New seller
            totalReviews = 0,
            memberSince = "2024",
            isVerified = false,
            responseTime = "New seller"
        )
    }
    
    private fun createSpecificationsFromForm(formState: SellFormState): List<Specification> {
        val specs = mutableListOf<Specification>()
        
        specs.add(Specification("Category", formState.category))
        specs.add(Specification("Condition", formState.condition))
        specs.add(Specification("Negotiable", if (formState.isNegotiable) "Yes" else "No"))
        
        formState.location?.let { location ->
            specs.add(Specification("Location", location.placeName))
        }
        
        return specs
    }
}
