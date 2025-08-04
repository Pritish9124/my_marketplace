package com.androstark.marketplace.presentation.sell

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androstark.marketplace.domain.model.*
import com.androstark.marketplace.domain.repository.MarketplaceRepository
import com.androstark.marketplace.domain.usecase.ImagePickerUseCase
import com.androstark.marketplace.domain.usecase.SubmitAdUseCase
import com.androstark.marketplace.domain.usecase.ValidateSellFormUseCase
import com.androstark.marketplace.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SellUiEvent {
    object NavigateToLocationPicker : SellUiEvent()
    object ShowImagePicker : SellUiEvent()
    data class NavigateToAdDetails(val adId: Int) : SellUiEvent()
    data class ShowError(val message: String) : SellUiEvent()
    data class ShowSuccess(val message: String) : SellUiEvent()
}

@HiltViewModel
class SellViewModel @Inject constructor(
    private val marketplaceRepository: MarketplaceRepository,
    private val validateSellFormUseCase: ValidateSellFormUseCase,
    private val submitAdUseCase: SubmitAdUseCase,
    private val imagePickerUseCase: ImagePickerUseCase
) : ViewModel() {

    private val _categoriesState = MutableStateFlow<UiState<List<Category>>>(UiState.Loading)
    val categoriesState: StateFlow<UiState<List<Category>>> = _categoriesState.asStateFlow()

    private val _formState = MutableStateFlow(SellFormState())
    val formState: StateFlow<SellFormState> = _formState.asStateFlow()
    
    private val _showValidationErrors = MutableStateFlow(false)
    val showValidationErrors: StateFlow<Boolean> = _showValidationErrors.asStateFlow()

    private val _uiEvent = MutableStateFlow<SellUiEvent?>(null)
    val uiEvent: StateFlow<SellUiEvent?> = _uiEvent.asStateFlow()

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()

    val conditions = listOf(
        "New", "Like New", "Good", "Fair", "Poor"
    )

    init {
        loadCategories()
        // Initialize with default contact info (in real app, get from user profile)
        updateContactInfo(
            ContactInfo(
                name = "John Doe",
                phone = "+91 9876543210",
                email = "john.doe@example.com"
            )
        )
    }

    fun onEvent(event: SellFormEvent) {
        when (event) {
            is SellFormEvent.TitleChanged -> updateTitle(event.title)
            is SellFormEvent.DescriptionChanged -> updateDescription(event.description)
            is SellFormEvent.PriceChanged -> updatePrice(event.price)
            is SellFormEvent.CategoryChanged -> updateCategory(event.category)
            is SellFormEvent.ConditionChanged -> updateCondition(event.condition)
            is SellFormEvent.LocationChanged -> updateLocation(event.location)
            is SellFormEvent.ImagesChanged -> updateImages(event.images)
            is SellFormEvent.NegotiableChanged -> updateNegotiable(event.isNegotiable)
            is SellFormEvent.ContactInfoChanged -> updateContactInfo(event.contactInfo)
            is SellFormEvent.RemoveImage -> removeImage(event.index)
            SellFormEvent.AddImage -> showImagePicker()
            SellFormEvent.SelectLocation -> showLocationPicker()
            SellFormEvent.SubmitForm -> submitForm()
            SellFormEvent.ValidateForm -> validateForm()
        }
    }

    private fun updateTitle(title: String) {
        _formState.value = _formState.value.copy(title = title)
        validateForm()
    }

    private fun updateDescription(description: String) {
        _formState.value = _formState.value.copy(description = description)
        validateForm()
    }

    private fun updatePrice(price: String) {
        _formState.value = _formState.value.copy(price = price)
        validateForm()
    }

    private fun updateCategory(category: String) {
        _formState.value = _formState.value.copy(category = category)
        validateForm()
    }

    private fun updateCondition(condition: String) {
        _formState.value = _formState.value.copy(condition = condition)
        validateForm()
    }

    private fun updateLocation(location: LocationData) {
        _formState.value = _formState.value.copy(location = location)
        validateForm()
    }

    private fun updateImages(images: List<String>) {
        _formState.value = _formState.value.copy(images = images)
        validateForm()
    }

    private fun updateNegotiable(isNegotiable: Boolean) {
        _formState.value = _formState.value.copy(isNegotiable = isNegotiable)
    }

    private fun updateContactInfo(contactInfo: ContactInfo) {
        _formState.value = _formState.value.copy(contactInfo = contactInfo)
        validateForm()
    }

    private fun removeImage(index: Int) {
        val currentImages = _formState.value.images.toMutableList()
        if (index in currentImages.indices) {
            currentImages.removeAt(index)
            updateImages(currentImages)
        }
    }

    private fun showImagePicker() {
        _uiEvent.value = SellUiEvent.ShowImagePicker
    }

    private fun showLocationPicker() {
        _uiEvent.value = SellUiEvent.NavigateToLocationPicker
    }

    private fun validateForm() {
        val validation = validateSellFormUseCase(_formState.value)
        _formState.value = _formState.value.copy(validation = validation)
    }
    
    fun resetValidationErrors() {
        _showValidationErrors.value = false
    }

    private fun submitForm() {
        validateForm()
        
        // Show validation errors when user clicks submit
        _showValidationErrors.value = true
        
        if (!_formState.value.validation.isValid) {
            _uiEvent.value = SellUiEvent.ShowError("Please fix the errors before submitting")
            return
        }

        viewModelScope.launch {
            _isSubmitting.value = true
            
            submitAdUseCase(_formState.value).fold(
                onSuccess = { createdAd ->
                    _isSubmitting.value = false
                    _uiEvent.value = SellUiEvent.ShowSuccess("Ad posted successfully!")
                    _uiEvent.value = SellUiEvent.NavigateToAdDetails(createdAd.id)
                },
                onFailure = { error ->
                    _isSubmitting.value = false
                    _uiEvent.value = SellUiEvent.ShowError(
                        error.message ?: "Failed to post ad. Please try again."
                    )
                }
            )
        }
    }

    fun onImageSelected(imageUri: Uri, context: Context) {
        viewModelScope.launch {
            try {
                // Validate image size
                if (!imagePickerUseCase.validateImageSize(context, imageUri)) {
                    _uiEvent.value = SellUiEvent.ShowError("Image size too large. Please select a smaller image.")
                    return@launch
                }

                // Compress image if needed
                val finalUri = imagePickerUseCase.compressImage(context, imageUri) ?: imageUri
                
                val currentImages = _formState.value.images.toMutableList()
                if (currentImages.size < 10) {
                    currentImages.add(finalUri.toString())
                    updateImages(currentImages)
                } else {
                    _uiEvent.value = SellUiEvent.ShowError("Maximum 10 images allowed")
                }
            } catch (e: Exception) {
                _uiEvent.value = SellUiEvent.ShowError("Failed to process image: ${e.message}")
            }
        }
    }

    fun clearUiEvent() {
        _uiEvent.value = null
    }

    fun loadCategories() {
        viewModelScope.launch {
            marketplaceRepository.getCategories().fold(
                onSuccess = { categories ->
                    _categoriesState.value = UiState.Success(categories)
                },
                onFailure = { error ->
                    _categoriesState.value = UiState.Error(
                        error.message ?: "Failed to load categories"
                    )
                }
            )
        }
    }

    fun retryLoadCategories() {
        loadCategories()
    }
}
