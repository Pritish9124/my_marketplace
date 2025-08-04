package com.androstark.marketplace.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androstark.marketplace.domain.model.ItemDetails
import com.androstark.marketplace.domain.repository.ChatRepository
import com.androstark.marketplace.domain.repository.WishlistRepository
import com.androstark.marketplace.domain.usecase.GetItemDetailsUseCase
import com.androstark.marketplace.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdDetailsViewModel @Inject constructor(
    private val getItemDetailsUseCase: GetItemDetailsUseCase,
    private val wishlistRepository: WishlistRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _itemDetailsState = MutableStateFlow<UiState<ItemDetails>>(UiState.Loading)
    val itemDetailsState: StateFlow<UiState<ItemDetails>> = _itemDetailsState.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private val _selectedImageIndex = MutableStateFlow(0)
    val selectedImageIndex: StateFlow<Int> = _selectedImageIndex.asStateFlow()

    fun loadItemDetails(itemId: Int) {
        viewModelScope.launch {
            _itemDetailsState.value = UiState.Loading
            
            getItemDetailsUseCase(itemId).fold(
                onSuccess = { itemDetails ->
                    _itemDetailsState.value = UiState.Success(itemDetails)
                    // Check if item is in wishlist
                    checkWishlistStatus(itemId)
                },
                onFailure = { error ->
                    _itemDetailsState.value = UiState.Error(
                        error.message ?: "Failed to load item details"
                    )
                }
            )
        }
    }

    private fun checkWishlistStatus(itemId: Int) {
        viewModelScope.launch {
            val isInWishlist = wishlistRepository.isItemInWishlist(itemId)
            _isFavorite.value = isInWishlist
        }
    }

    fun toggleFavorite() {
        val currentState = _itemDetailsState.value
        if (currentState is UiState.Success) {
            viewModelScope.launch {
                wishlistRepository.toggleWishlist(currentState.data).fold(
                    onSuccess = { isNowFavorite ->
                        _isFavorite.value = isNowFavorite
                    },
                    onFailure = {
                        // Revert the UI state if the operation failed
                        // Could show a toast or error message here
                    }
                )
            }
        }
    }

    fun selectImage(index: Int) {
        _selectedImageIndex.value = index
    }

    fun retryLoadItemDetails(itemId: Int) {
        loadItemDetails(itemId)
    }

    fun onContactSellerClick(): Int? {
        val currentState = _itemDetailsState.value
        if (currentState is UiState.Success) {
            val itemDetails = currentState.data
            viewModelScope.launch {
                chatRepository.createConversation(
                    itemId = itemDetails.id,
                    sellerId = itemDetails.seller.id,
                    initialMessage = "Hi! I'm interested in your ${itemDetails.title}. Is it still available?"
                )
            }
            return itemDetails.id
        }
        return null
    }

    fun onCallSellerClick() {
        // Handle call seller action
        // In real app, this would initiate a phone call
    }

    fun getShareContent(): ShareContent? {
        val currentState = _itemDetailsState.value
        if (currentState is UiState.Success) {
            val itemDetails = currentState.data
            return ShareContent(
                title = "Check out this ${itemDetails.title}",
                text = "${itemDetails.title}\n\n${itemDetails.currency}${itemDetails.price.toInt()}\n${itemDetails.location}\n\nDownload MarketPlace app: https://play.google.com/store/apps/details?id=com.androstark.marketplace",
                imageUrl = itemDetails.images.firstOrNull() ?: ""
            )
        }
        return null
    }

    data class ShareContent(
        val title: String,
        val text: String,
        val imageUrl: String
    )
}
