package com.androstark.marketplace.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.androstark.marketplace.domain.model.MarketplaceItem
import com.androstark.marketplace.presentation.common.UiState
import com.androstark.marketplace.presentation.components.EmptyStateComponent
import com.androstark.marketplace.presentation.components.ErrorComponent
import com.androstark.marketplace.presentation.components.ListItemCard
import com.androstark.marketplace.presentation.components.LoadingComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdListScreen(
    category: String? = null,
    onBackClick: () -> Unit,
    onItemClick: (Int) -> Unit,
    viewModel: AdListViewModel = hiltViewModel()
) {
    val itemsState by viewModel.itemsState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val wishlistItems by viewModel.wishlistItems.collectAsStateWithLifecycle()

    LaunchedEffect(category) {
        viewModel.loadItems(category)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = category ?: "All Items",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = { /* TODO: Implement filter */ }) {
                    Icon(Icons.Default.FilterList, contentDescription = "Filter")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        // Content
        when (val currentState = itemsState) {
            is UiState.Loading -> {
                LoadingComponent(
                    modifier = Modifier.fillMaxSize(),
                    message = "Loading items..."
                )
            }
            
            is UiState.Error -> {
                ErrorComponent(
                    message = currentState.message,
                    onRetryClick = { viewModel.retryLoadItems(category) },
                    modifier = Modifier.fillMaxSize()
                )
            }
            
            is UiState.Success -> {
                val items = currentState.data
                
                if (items.isEmpty()) {
                    EmptyStateComponent(
                        title = "No Items Found",
                        subtitle = if (category != null) {
                            "No items found in $category category"
                        } else {
                            "No items available at the moment"
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Items count header
                        item {
                            Text(
                                text = "${items.size} items found",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        
                        // Items list
                        items(items) { item ->
                            ListItemCard(
                                item = item,
                                onClick = { onItemClick(item.id) },
                                onFavoriteClick = { viewModel.toggleWishlist(item) },
                                isFavorite = wishlistItems.contains(item.id)
                            )
                        }
                        
                        // Bottom spacing
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
            
            is UiState.Empty -> {
                EmptyStateComponent(
                    title = "No Items Found",
                    subtitle = if (category != null) {
                        "No items found in $category category"
                    } else {
                        "No items available at the moment"
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
