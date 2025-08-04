package com.androstark.marketplace.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androstark.marketplace.domain.model.MarketplaceItem
import com.androstark.marketplace.domain.repository.MarketplaceRepository
import com.androstark.marketplace.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdListViewModel @Inject constructor(
    private val marketplaceRepository: MarketplaceRepository,
    private val wishlistRepository: com.androstark.marketplace.domain.repository.WishlistRepository
) : ViewModel() {

    private val _itemsState = MutableStateFlow<UiState<List<MarketplaceItem>>>(UiState.Loading)
    val itemsState: StateFlow<UiState<List<MarketplaceItem>>> = _itemsState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _wishlistItems = MutableStateFlow<Set<Int>>(emptySet())
    val wishlistItems: StateFlow<Set<Int>> = _wishlistItems.asStateFlow()

    init {
        loadWishlistItems()
    }

    fun loadItems(category: String? = null) {
        viewModelScope.launch {
            _itemsState.value = UiState.Loading
            
            if (category != null) {
                // Load items by category
                marketplaceRepository.getItemsByCategory(getCategoryId(category)).fold(
                    onSuccess = { items ->
                        if (items.isEmpty()) {
                            _itemsState.value = UiState.Empty
                        } else {
                            _itemsState.value = UiState.Success(items)
                        }
                    },
                    onFailure = { error ->
                        _itemsState.value = UiState.Error(
                            error.message ?: "Failed to load items"
                        )
                    }
                )
            } else {
                // Load all featured items (for "View All" functionality)
                marketplaceRepository.getFeaturedItems().fold(
                    onSuccess = { items ->
                        if (items.isEmpty()) {
                            _itemsState.value = UiState.Empty
                        } else {
                            _itemsState.value = UiState.Success(items)
                        }
                    },
                    onFailure = { error ->
                        _itemsState.value = UiState.Error(
                            error.message ?: "Failed to load items"
                        )
                    }
                )
            }
        }
    }

    fun retryLoadItems(category: String? = null) {
        loadItems(category)
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        // TODO: Implement search functionality
    }

    private fun loadWishlistItems() {
        viewModelScope.launch {
            try {
                wishlistRepository.getWishlistItems().collect { items ->
                    _wishlistItems.value = items.map { it.id }.toSet()
                }
            } catch (e: Exception) {
                // Handle error silently for wishlist
            }
        }
    }

    fun toggleWishlist(item: MarketplaceItem) {
        viewModelScope.launch {
            val isCurrentlyInWishlist = _wishlistItems.value.contains(item.id)
            
            // Convert MarketplaceItem to ItemDetails for repository
            val itemDetails = com.androstark.marketplace.domain.model.ItemDetails(
                id = item.id,
                title = item.title,
                price = item.price,
                currency = item.currency,
                location = item.location,
                timePosted = item.timePosted,
                images = listOf(item.imageUrl),
                isFeatured = item.isFeatured,
                isHot = item.isHot,
                category = item.category,
                condition = item.condition,
                description = item.description,
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
            
            if (isCurrentlyInWishlist) {
                wishlistRepository.removeFromWishlist(item.id).fold(
                    onSuccess = {
                        _wishlistItems.value = _wishlistItems.value - item.id
                    },
                    onFailure = {
                        // Handle error
                    }
                )
            } else {
                wishlistRepository.addToWishlist(itemDetails).fold(
                    onSuccess = {
                        _wishlistItems.value = _wishlistItems.value + item.id
                    },
                    onFailure = {
                        // Handle error
                    }
                )
            }
        }
    }

    private fun getCategoryId(categoryName: String): Int {
        // Map category names to IDs - in real app this would come from the categories API
        return when (categoryName.lowercase()) {
            "electronics" -> 1
            "vehicles" -> 2
            "furniture" -> 3
            "fashion" -> 4
            "books" -> 5
            "sports" -> 6
            "home & garden" -> 7
            "pets" -> 8
            else -> 0 // All categories
        }
    }
}
