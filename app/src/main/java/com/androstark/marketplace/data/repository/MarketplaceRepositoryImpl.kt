package com.androstark.marketplace.data.repository

import com.androstark.marketplace.data.dto.CategoryDto
import com.androstark.marketplace.data.dto.ItemDetailsDto
import com.androstark.marketplace.data.dto.MarketplaceItemDto
import com.androstark.marketplace.data.dto.SellerDto
import com.androstark.marketplace.data.dto.SpecificationDto
import com.androstark.marketplace.data.dto.toDomain
import com.androstark.marketplace.domain.model.Category
import com.androstark.marketplace.domain.model.ItemDetails
import com.androstark.marketplace.domain.model.MarketplaceItem
import com.androstark.marketplace.domain.repository.MarketplaceRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarketplaceRepositoryImpl @Inject constructor() : MarketplaceRepository {

    // Mock data - In real app, this would come from API
    private val mockCategories = listOf(
        CategoryDto(1, "Vehicles", "vehicles", "#3B82F6", 1234),
        CategoryDto(2, "Property", "property", "#10B981", 856),
        CategoryDto(3, "Electronics", "electronics", "#8B5CF6", 2341),
        CategoryDto(4, "Fashion", "fashion", "#F59E0B", 987),
        CategoryDto(5, "Furniture", "furniture", "#EF4444", 567),
        CategoryDto(6, "Cameras", "cameras", "#06B6D4", 432),
        CategoryDto(7, "Gaming", "gaming", "#EC4899", 765),
        CategoryDto(8, "Books", "books", "#84CC16", 345)
    )

    private val mockFeaturedItems = listOf(
        MarketplaceItemDto(
            id = 1,
            title = "iPhone 14 Pro Max 256GB - Like New Condition",
            price = 899.0,
            currency = "₹",
            location = "Bengaluru, India",
            timePosted = "2h",
            imageUrl = "https://images.unsplash.com/photo-1592750475338-74b7b21085ab?w=400",
            isFeatured = true,
            isHot = true,
            category = "Electronics",
            condition = "Like New",
            description = "iPhone 14 Pro Max in excellent condition, barely used. Comes with original box and charger."
        ),
        MarketplaceItemDto(
            id = 2,
            title = "2018 Honda Civic - Excellent Condition",
            price = 18500.0,
            currency = "₹",
            location = "Los Angeles, CA",
            timePosted = "4h",
            imageUrl = "https://images.unsplash.com/photo-1552519507-da3b142c6e3d?w=400",
            isFeatured = true,
            isHot = false,
            category = "Vehicles",
            condition = "Excellent",
            description = "2018 Honda Civic with low mileage, well maintained, single owner."
        ),
        MarketplaceItemDto(
            id = 3,
            title = "MacBook Pro 13-inch M2 - Perfect for Students",
            price = 1299.0,
            currency = "₹",
            location = "Chicago, IL",
            timePosted = "1d",
            imageUrl = "https://images.unsplash.com/photo-1541807084-5c52b6b3adef?w=400",
            isFeatured = true,
            isHot = false,
            category = "Electronics",
            condition = "Excellent",
            description = "MacBook Pro M2 chip, 8GB RAM, 256GB SSD. Perfect for students and professionals."
        ),
        MarketplaceItemDto(
            id = 4,
            title = "Vintage Leather Sofa Set - 3 Pieces",
            price = 750.0,
            currency = "₹",
            location = "Miami, FL",
            timePosted = "3d",
            imageUrl = "https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=400",
            isFeatured = true,
            isHot = false,
            category = "Furniture",
            condition = "Good",
            description = "Beautiful vintage leather sofa set, includes 3-seater, 2-seater, and armchair."
        )
    )

    // Mock detailed item data
    private val mockItemDetails = mapOf(
        1 to ItemDetailsDto(
            id = 1,
            title = "iPhone 14 Pro Max 256GB - Like New Condition",
            price = 899.0,
            currency = "₹",
            location = "Bengaluru, India",
            timePosted = "2h",
            images = listOf(
                "https://images.unsplash.com/photo-1592750475338-74b7b21085ab?w=800",
                "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=800",
                "https://images.unsplash.com/photo-1580910051074-3eb694886505?w=800"
            ),
            isFeatured = true,
            isHot = true,
            category = "Electronics",
            condition = "Like New",
            description = "iPhone 14 Pro Max in excellent condition, barely used. Comes with original box and charger. All accessories included. No scratches or damage. Perfect working condition with 98% battery health.",
            seller = SellerDto(
                id = 101,
                name = "John Smith",
                avatar = "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=100",
                rating = 4.8,
                totalReviews = 24,
                memberSince = "2022",
                isVerified = true,
                responseTime = "Usually responds within 2 hours"
            ),
            specifications = listOf(
                SpecificationDto("Brand", "Apple"),
                SpecificationDto("Model", "iPhone 14 Pro Max"),
                SpecificationDto("Storage", "256GB"),
                SpecificationDto("Color", "Deep Purple"),
                SpecificationDto("Condition", "Like New"),
                SpecificationDto("Battery Health", "98%")
            ),
            views = 156,
            favorites = 23,
            isNegotiable = true
        ),
        2 to ItemDetailsDto(
            id = 2,
            title = "Used Bicycle",
            price = 1500.0,
            currency = "₹",
            location = "Delhi, India",
            timePosted = "2 days ago",
            images = listOf(
                "https://images.unsplash.com/photo-1558618047-3c8c76ca7d13?w=800",
                "https://images.unsplash.com/photo-1571068316344-75bc76f77890?w=800"
            ),
            isFeatured = false,
            isHot = false,
            category = "Vehicles",
            condition = "Good",
            description = "This bicycle is in good condition and has been used for 2 years. It's perfect for commuting or leisure rides. Some minor scratches but fully functional.",
            seller = SellerDto(
                id = 102,
                name = "Priya Sharma",
                avatar = "https://images.unsplash.com/photo-1494790108755-2616b612b3c5?w=100",
                rating = 4.5,
                totalReviews = 12,
                memberSince = "2023",
                isVerified = true,
                responseTime = "Usually responds within 4 hours"
            ),
            specifications = listOf(
                SpecificationDto("Type", "City Bike"),
                SpecificationDto("Frame Size", "Medium"),
                SpecificationDto("Gear", "Single Speed"),
                SpecificationDto("Age", "2 years"),
                SpecificationDto("Condition", "Good")
            ),
            views = 89,
            favorites = 7,
            isNegotiable = true
        ),
        3 to ItemDetailsDto(
            id = 3,
            title = "MacBook Pro 13-inch M2 - Perfect for Students",
            price = 1299.0,
            currency = "₹",
            location = "Chicago, IL",
            timePosted = "1d",
            images = listOf(
                "https://images.unsplash.com/photo-1541807084-5c52b6b3adef?w=800",
                "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=800",
                "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=800"
            ),
            isFeatured = true,
            isHot = false,
            category = "Electronics",
            condition = "Excellent",
            description = "MacBook Pro M2 chip, 8GB RAM, 256GB SSD. Perfect for students and professionals. Excellent condition with minimal usage. Comes with original charger and box.",
            seller = SellerDto(
                id = 103,
                name = "Mike Johnson",
                avatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=100",
                rating = 4.9,
                totalReviews = 31,
                memberSince = "2021",
                isVerified = true,
                responseTime = "Usually responds within 1 hour"
            ),
            specifications = listOf(
                SpecificationDto("Brand", "Apple"),
                SpecificationDto("Model", "MacBook Pro 13-inch"),
                SpecificationDto("Processor", "Apple M2"),
                SpecificationDto("RAM", "8GB"),
                SpecificationDto("Storage", "256GB SSD"),
                SpecificationDto("Year", "2023")
            ),
            views = 234,
            favorites = 45,
            isNegotiable = false
        )
    )

    private fun getItemDetailsById(itemId: Int): ItemDetailsDto? {
        return mockItemDetails[itemId]
    }

    override suspend fun getCategories(): Result<List<Category>> {
        return try {
            delay(500) // Simulate network delay
            Result.success(mockCategories.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFeaturedItems(): Result<List<MarketplaceItem>> {
        return try {
            delay(800) // Simulate network delay
            Result.success(mockFeaturedItems.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getItemDetails(itemId: Int): Result<ItemDetails> {
        return try {
            delay(1000) // Simulate network delay
            val itemDetails = getItemDetailsById(itemId)
            if (itemDetails != null) {
                Result.success(itemDetails.toDomain())
            } else {
                Result.failure(Exception("Item not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchItems(query: String): Result<List<MarketplaceItem>> {
        return try {
            delay(600)
            val filteredItems = mockFeaturedItems.filter { 
                it.title.contains(query, ignoreCase = true) ||
                it.category.contains(query, ignoreCase = true)
            }
            Result.success(filteredItems.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



    override suspend fun getItemsByCategory(categoryId: Int): Result<List<MarketplaceItem>> {
        return try {
            delay(600)
            val categoryName = mockCategories.find { it.id == categoryId }?.name ?: ""
            val filteredItems = mockFeaturedItems.filter { 
                it.category.equals(categoryName, ignoreCase = true)
            }
            Result.success(filteredItems.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
