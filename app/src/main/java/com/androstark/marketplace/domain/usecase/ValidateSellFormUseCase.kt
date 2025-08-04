package com.androstark.marketplace.domain.usecase

import com.androstark.marketplace.domain.model.FormValidation
import com.androstark.marketplace.domain.model.SellFormState
import javax.inject.Inject

class ValidateSellFormUseCase @Inject constructor() {
    
    operator fun invoke(formState: SellFormState): FormValidation {
        val titleError = validateTitle(formState.title)
        val descriptionError = validateDescription(formState.description)
        val priceError = validatePrice(formState.price)
        val categoryError = validateCategory(formState.category)
        val conditionError = validateCondition(formState.condition)
        val locationError = validateLocation(formState.location)
        val imagesError = validateImages(formState.images)
        val contactError = validateContactInfo(formState.contactInfo)
        
        val isValid = listOf(
            titleError, descriptionError, priceError, categoryError,
            conditionError, locationError, imagesError, contactError
        ).all { it == null }
        
        return FormValidation(
            titleError = titleError,
            descriptionError = descriptionError,
            priceError = priceError,
            categoryError = categoryError,
            conditionError = conditionError,
            locationError = locationError,
            imagesError = imagesError,
            contactError = contactError,
            isValid = isValid
        )
    }
    
    private fun validateTitle(title: String): String? {
        return when {
            title.isBlank() -> "Title is required"
            title.length < 5 -> "Title must be at least 5 characters"
            title.length > 100 -> "Title must be less than 100 characters"
            else -> null
        }
    }
    
    private fun validateDescription(description: String): String? {
        return when {
            description.isBlank() -> "Description is required"
            description.length < 10 -> "Description must be at least 10 characters"
            description.length > 1000 -> "Description must be less than 1000 characters"
            else -> null
        }
    }
    
    private fun validatePrice(price: String): String? {
        return when {
            price.isBlank() -> "Price is required"
            price.toDoubleOrNull() == null -> "Please enter a valid price"
            price.toDouble() <= 0 -> "Price must be greater than 0"
            price.toDouble() > 10000000 -> "Price seems too high"
            else -> null
        }
    }
    
    private fun validateCategory(category: String): String? {
        return if (category.isBlank()) "Please select a category" else null
    }
    
    private fun validateCondition(condition: String): String? {
        return if (condition.isBlank()) "Please select item condition" else null
    }
    
    private fun validateLocation(location: com.androstark.marketplace.domain.model.LocationData?): String? {
        return if (location == null) "Please select a location" else null
    }
    
    private fun validateImages(images: List<String>): String? {
        return when {
            images.isEmpty() -> "Please add at least one photo"
            images.size > 10 -> "Maximum 10 photos allowed"
            else -> null
        }
    }
    
    private fun validateContactInfo(contactInfo: com.androstark.marketplace.domain.model.ContactInfo): String? {
        return when {
            contactInfo.name.isBlank() -> "Contact name is required"
            contactInfo.phone.isBlank() -> "Phone number is required"
            !isValidPhoneNumber(contactInfo.phone) -> "Please enter a valid phone number"
            contactInfo.email.isNotBlank() && !isValidEmail(contactInfo.email) -> "Please enter a valid email"
            else -> null
        }
    }
    
    private fun isValidPhoneNumber(phone: String): Boolean {
        val phoneRegex = "^[+]?[0-9]{10,15}$".toRegex()
        return phone.matches(phoneRegex)
    }
    
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return email.matches(emailRegex)
    }
}
