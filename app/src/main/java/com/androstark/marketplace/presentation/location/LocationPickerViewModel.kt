package com.androstark.marketplace.presentation.location

import android.content.Context
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androstark.marketplace.domain.model.LocationData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class LocationPickerUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentLocation: LatLng? = null,
    val selectedLocation: LocationData? = null
)

@HiltViewModel
class LocationPickerViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(LocationPickerUiState())
    val uiState: StateFlow<LocationPickerUiState> = _uiState.asStateFlow()
    
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    
    fun getCurrentLocation(context: Context) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)

                // Permission check
                val hasFineLocation = androidx.core.content.ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                val hasCoarseLocation = androidx.core.content.ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                if (!hasFineLocation && !hasCoarseLocation) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Location permission not granted."
                    )
                    return@launch
                }

                fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                val location = fusedLocationClient.lastLocation.result
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    val locationData = getLocationDataFromLatLng(context, latLng)
                    
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        currentLocation = latLng,
                        selectedLocation = locationData
                    )
                } else {
                    // Default to Delhi if location not available
                    val defaultLocation = LatLng(28.6139, 77.2090)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        currentLocation = defaultLocation,
                        error = "Could not get current location. Please select manually."
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to get location: ${e.message}"
                )
            }
        }
    }
    
    fun onLocationSelected(latLng: LatLng, context: Context) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                
                val locationData = getLocationDataFromLatLng(context, latLng)
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    selectedLocation = locationData
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to get location details: ${e.message}"
                )
            }
        }
    }
    
    private suspend fun getLocationDataFromLatLng(context: Context, latLng: LatLng): LocationData {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val fullAddress = buildString {
                    address.getAddressLine(0)?.let { append(it) }
                }
                
                val placeName = buildString {
                    address.locality?.let { append(it) }
                    if (isNotEmpty() && address.adminArea != null) append(", ")
                    address.adminArea?.let { append(it) }
                    if (isNotEmpty() && address.countryName != null) append(", ")
                    address.countryName?.let { append(it) }
                }
                
                LocationData(
                    address = fullAddress.ifEmpty { "Unknown Address" },
                    latitude = latLng.latitude,
                    longitude = latLng.longitude,
                    placeName = placeName.ifEmpty { "Unknown Place" }
                )
            } else {
                LocationData(
                    address = "Unknown Address",
                    latitude = latLng.latitude,
                    longitude = latLng.longitude,
                    placeName = "Selected Location"
                )
            }
        } catch (e: Exception) {
            LocationData(
                address = "Unknown Address",
                latitude = latLng.latitude,
                longitude = latLng.longitude,
                placeName = "Selected Location"
            )
        }
    }
}
