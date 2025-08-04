package com.androstark.marketplace.presentation.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androstark.marketplace.domain.model.MarketplaceItem
import com.androstark.marketplace.domain.repository.WishlistRepository
import com.androstark.marketplace.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val wishlistRepository: WishlistRepository
) : ViewModel() {

    private val _wishlistState = MutableStateFlow<UiState<List<MarketplaceItem>>>(UiState.Loading)
    val wishlistState: StateFlow<UiState<List<MarketplaceItem>>> = _wishlistState.asStateFlow()

    init {
        loadWishlistItems()
    }

    fun loadWishlistItems() {
        viewModelScope.launch {
            _wishlistState.value = UiState.Loading

            try {
                wishlistRepository.getWishlistItems().collect { items ->
                    if (items.isEmpty()) {
                        _wishlistState.value = UiState.Empty
                    } else {
                        // Convert ItemDetails to MarketplaceItem
                        val marketplaceItems = items.map { itemDetails ->
                            MarketplaceItem(
                                id = itemDetails.id,
                                title = itemDetails.title,
                                price = itemDetails.price,
                                currency = itemDetails.currency,
                                location = itemDetails.location,
                                timePosted = itemDetails.timePosted,
                                imageUrl = itemDetails.images.firstOrNull() ?: "",
                                isFeatured = itemDetails.isFeatured,
                                isHot = itemDetails.isHot,
                                category = itemDetails.category,
                                condition = itemDetails.condition,
                                description = itemDetails.description
                            )
                        }
                        _wishlistState.value = UiState.Success(marketplaceItems)
                    }
                }
            } catch (error: Exception) {
                _wishlistState.value = UiState.Error(
                    error.message ?: "Failed to load saved items"
                )
            }
        }
    }

    fun removeFromWishlist(item: MarketplaceItem) {
        viewModelScope.launch {
            wishlistRepository.removeFromWishlist(item.id).fold(
                onSuccess = {
                    // Reload the wishlist to update the UI
                    loadWishlistItems()
                },
                onFailure = { error ->
                    // Handle error - could show a snackbar or toast
                    // For now, just reload to ensure consistency
                    loadWishlistItems()
                }
            )
        }
    }

    fun retryLoadWishlistItems() {
        loadWishlistItems()
    }
}
