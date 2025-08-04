package com.androstark.marketplace.domain.model

data class SellFormState(
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val category: String = "",
    val condition: String = "",
    val location: LocationData? = null,
    val images: List<String> = emptyList(),
    val isNegotiable: Boolean = false,
    val contactInfo: ContactInfo = ContactInfo(),
    val validation: FormValidation = FormValidation()
)

data class LocationData(
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val placeName: String
)

data class ContactInfo(
    val name: String = "",
    val phone: String = "",
    val email: String = ""
)

data class FormValidation(
    val titleError: String? = null,
    val descriptionError: String? = null,
    val priceError: String? = null,
    val categoryError: String? = null,
    val conditionError: String? = null,
    val locationError: String? = null,
    val imagesError: String? = null,
    val contactError: String? = null,
    val isValid: Boolean = false
)

sealed class SellFormEvent {
    data class TitleChanged(val title: String) : SellFormEvent()
    data class DescriptionChanged(val description: String) : SellFormEvent()
    data class PriceChanged(val price: String) : SellFormEvent()
    data class CategoryChanged(val category: String) : SellFormEvent()
    data class ConditionChanged(val condition: String) : SellFormEvent()
    data class LocationChanged(val location: LocationData) : SellFormEvent()
    data class ImagesChanged(val images: List<String>) : SellFormEvent()
    data class NegotiableChanged(val isNegotiable: Boolean) : SellFormEvent()
    data class ContactInfoChanged(val contactInfo: ContactInfo) : SellFormEvent()
    object AddImage : SellFormEvent()
    data class RemoveImage(val index: Int) : SellFormEvent()
    object SelectLocation : SellFormEvent()
    object SubmitForm : SellFormEvent()
    object ValidateForm : SellFormEvent()
}
