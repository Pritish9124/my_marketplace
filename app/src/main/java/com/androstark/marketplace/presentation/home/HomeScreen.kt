package com.androstark.marketplace.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.androstark.marketplace.R
import com.androstark.marketplace.domain.model.Category
import com.androstark.marketplace.domain.model.MarketplaceItem
import com.androstark.marketplace.presentation.common.UiState
import com.androstark.marketplace.presentation.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToChat: () -> Unit = {},
    onNavigateToSell: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onItemClick: (Int) -> Unit = {},
    onCategoryClick: (com.androstark.marketplace.domain.model.Category) -> Unit = {},
    onViewAllClick: () -> Unit = {}
) {
    val categoriesState by viewModel.categoriesState.collectAsStateWithLifecycle()
    val featuredItemsState by viewModel.featuredItemsState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                    Text(
                        text = stringResource(R.string.city),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            actions = {
                IconButton(onClick = { /* Handle notifications */ }) {
                    Badge {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications"
                        )
                    }
                }
                IconButton(onClick = onNavigateToChat) {
                    Icon(
                        imageVector = Icons.Default.Chat,
                        contentDescription = "Chat"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Search Bar
            item {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = viewModel::onSearchQueryChanged,
                    placeholder = "What are you looking for?",
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            // Categories Section
            item {
                CategoriesSection(
                    categoriesState = categoriesState,
                    onCategoryClick = onCategoryClick,
                    onRetryClick = viewModel::retryLoadCategories
                )
            }
            
            // Featured Section
            item {
                FeaturedSection(
                    featuredItemsState = featuredItemsState,
                    onItemClick = { item -> onItemClick(item.id) },
                    onRetryClick = viewModel::retryLoadFeaturedItems,
                    onViewAllClick = onViewAllClick
                )
            }
        }
        

    }
}

@OptIn(ExperimentalMaterial3Api::class)


@Composable
private fun CategoriesSection(
    categoriesState: UiState<List<Category>>,
    onCategoryClick: (Category) -> Unit,
    onRetryClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Categories",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            TextButton(onClick = { /* Handle view all */ }) {
                Text("All")
                Icon(
                    imageVector = Icons.Default.GridView,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        when (categoriesState) {
            is UiState.Loading -> {
                LoadingComponent(
                    modifier = Modifier.height(200.dp),
                    message = "Loading categories..."
                )
            }
            is UiState.Success -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier.height(200.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(categoriesState.data) { category ->
                        CategoryItem(
                            category = category,
                            onClick = { onCategoryClick(category) }
                        )
                    }
                }
            }
            is UiState.Error -> {
                ErrorComponent(
                    message = categoriesState.message,
                    onRetryClick = onRetryClick,
                    modifier = Modifier.height(200.dp)
                )
            }
            is UiState.Empty -> {
                EmptyStateComponent(
                    title = "No Categories",
                    subtitle = "Categories will appear here",
                    modifier = Modifier.height(200.dp)
                )
            }
        }
    }
}



@Composable
private fun FeaturedSection(
    featuredItemsState: UiState<List<MarketplaceItem>>,
    onItemClick: (MarketplaceItem) -> Unit,
    onRetryClick: () -> Unit,
    onViewAllClick: () -> Unit = {}
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Featured",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                if (featuredItemsState is UiState.Success && featuredItemsState.data.any { it.isHot }) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = androidx.compose.ui.graphics.Color(0xFFFF5722),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Hot",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            color = androidx.compose.ui.graphics.Color.White,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
            
            TextButton(onClick = onViewAllClick) {
                Text("View All")
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        when (featuredItemsState) {
            is UiState.Loading -> {
                LoadingComponent(
                    modifier = Modifier.height(200.dp),
                    message = "Loading featured items..."
                )
            }
            is UiState.Success -> {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(featuredItemsState.data) { item ->
                        FeaturedItemCard(
                            item = item,
                            onClick = { onItemClick(item) }
                        )
                    }
                }
            }
            is UiState.Error -> {
                ErrorComponent(
                    message = featuredItemsState.message,
                    onRetryClick = onRetryClick,
                    modifier = Modifier.height(200.dp)
                )
            }
            is UiState.Empty -> {
                EmptyStateComponent(
                    title = "No Featured Items",
                    subtitle = "Featured items will appear here",
                    modifier = Modifier.height(200.dp)
                )
            }
        }
    }
}




