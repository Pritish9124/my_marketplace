package com.androstark.marketplace.presentation.saved

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.androstark.marketplace.presentation.common.UiState
import com.androstark.marketplace.presentation.components.EmptyStateComponent
import com.androstark.marketplace.presentation.components.ErrorComponent
import com.androstark.marketplace.presentation.components.ListItemCard
import com.androstark.marketplace.presentation.components.LoadingComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedScreen(
    onItemClick: (Int) -> Unit,
    viewModel: SavedViewModel = hiltViewModel()
) {
    val wishlistState by viewModel.wishlistState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Saved Items",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        // Content
        when (val currentState = wishlistState) {
            is UiState.Loading -> {
                LoadingComponent(
                    modifier = Modifier.fillMaxSize()
                )
            }
            
            is UiState.Error -> {
                ErrorComponent(
                    message = currentState.message,
                    onRetryClick = viewModel::loadWishlistItems,
                    modifier = Modifier.fillMaxSize()
                )
            }
            
            is UiState.Success -> {
                val items = currentState.data
                
                if (items.isEmpty()) {
                    EmptyStateComponent(
                        title = "No Saved Items",
                        subtitle = "Items you save will appear here for easy access",
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
                                text = "${items.size} saved items",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        
                        // Wishlist items
                        items(items) { item ->
                            ListItemCard(
                                item = item,
                                onClick = { onItemClick(item.id) },
                                onFavoriteClick = { viewModel.removeFromWishlist(item) },
                                isFavorite = true // All items in saved screen are favorites
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
                    title = "No Saved Items",
                    subtitle = "Items you save will appear here for easy access",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
