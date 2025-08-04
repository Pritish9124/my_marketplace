package com.androstark.marketplace.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androstark.marketplace.domain.model.Category
import com.androstark.marketplace.domain.model.MarketplaceItem
import com.androstark.marketplace.domain.usecase.GetCategoriesUseCase
import com.androstark.marketplace.domain.usecase.GetFeaturedItemsUseCase
import com.androstark.marketplace.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getFeaturedItemsUseCase: GetFeaturedItemsUseCase
) : ViewModel() {

    private val _categoriesState = MutableStateFlow<UiState<List<Category>>>(UiState.Loading)
    val categoriesState: StateFlow<UiState<List<Category>>> = _categoriesState.asStateFlow()
    
    private val _featuredItemsState = MutableStateFlow<UiState<List<MarketplaceItem>>>(UiState.Loading)
    val featuredItemsState: StateFlow<UiState<List<MarketplaceItem>>> = _featuredItemsState.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        loadCategories()
        loadFeaturedItems()
    }
    
    private fun loadCategories() {
        viewModelScope.launch {
            _categoriesState.value = UiState.Loading
            
            getCategoriesUseCase().fold(
                onSuccess = { categories ->
                    _categoriesState.value = UiState.Success(categories)
                },
                onFailure = { error ->
                    _categoriesState.value = UiState.Error(error.message ?: "Failed to load categories")
                }
            )
        }
    }
    
    private fun loadFeaturedItems() {
        viewModelScope.launch {
            _featuredItemsState.value = UiState.Loading
            
            getFeaturedItemsUseCase().fold(
                onSuccess = { items ->
                    _featuredItemsState.value = UiState.Success(items)
                },
                onFailure = { error ->
                    _featuredItemsState.value = UiState.Error(error.message ?: "Failed to load featured items")
                }
            )
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onCategorySelected(category: Category) {
        // Handle category selection - could navigate to category screen
    }

    fun onItemSelected(item: MarketplaceItem) {
        // Handle item selection - could navigate to item details
    }

    fun retryLoadCategories() {
        loadCategories()
    }
    
    fun retryLoadFeaturedItems() {
        loadFeaturedItems()
    }
}


