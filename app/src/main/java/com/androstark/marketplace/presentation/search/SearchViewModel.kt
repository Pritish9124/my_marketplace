package com.androstark.marketplace.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androstark.marketplace.domain.model.Category
import com.androstark.marketplace.domain.model.MarketplaceItem
import com.androstark.marketplace.domain.usecase.GetCategoriesUseCase
import com.androstark.marketplace.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val searchQuery: String = "",
    val selectedCategory: String = "",
    val categories: UiState<List<Category>> = UiState.Loading,
    val searchResults: UiState<List<MarketplaceItem>> = UiState.Empty,
    val recentSearches: List<String> = emptyList()
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(categories = UiState.Loading)
            
            getCategoriesUseCase().fold(
                onSuccess = { categories ->
                    _uiState.value = _uiState.value.copy(
                        categories = UiState.Success(categories)
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        categories = UiState.Error(error.message ?: "Failed to load categories")
                    )
                }
            )
        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun selectCategory(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
        performSearch()
    }

    fun performSearch() {
        val query = _uiState.value.searchQuery.trim()
        if (query.isEmpty() && _uiState.value.selectedCategory.isEmpty()) {
            _uiState.value = _uiState.value.copy(searchResults = UiState.Empty)
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(searchResults = UiState.Loading)
            
            try {
                // Add to recent searches
                addToRecentSearches(query)
                
                // Simulate API call - In real app, use search use case
                kotlinx.coroutines.delay(1000)
                
                // Mock search results
                val mockResults = emptyList<MarketplaceItem>()
                
                _uiState.value = _uiState.value.copy(
                    searchResults = if (mockResults.isEmpty()) {
                        UiState.Empty
                    } else {
                        UiState.Success(mockResults)
                    }
                )
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    searchResults = UiState.Error("Search failed: ${e.message}")
                )
            }
        }
    }

    private fun addToRecentSearches(query: String) {
        if (query.isNotBlank()) {
            val currentSearches = _uiState.value.recentSearches.toMutableList()
            currentSearches.remove(query) // Remove if already exists
            currentSearches.add(0, query) // Add to beginning
            
            // Keep only last 10 searches
            if (currentSearches.size > 10) {
                currentSearches.removeAt(currentSearches.size - 1)
            }
            
            _uiState.value = _uiState.value.copy(recentSearches = currentSearches)
        }
    }

    fun clearSearch() {
        _uiState.value = _uiState.value.copy(
            searchQuery = "",
            selectedCategory = "",
            searchResults = UiState.Empty
        )
    }

    fun retryLoadCategories() {
        loadCategories()
    }
}
