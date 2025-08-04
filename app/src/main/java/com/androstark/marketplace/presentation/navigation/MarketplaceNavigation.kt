package com.androstark.marketplace.presentation.navigation

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.androstark.marketplace.presentation.chat.ChatScreen
import com.androstark.marketplace.presentation.details.AdDetailsScreen
import com.androstark.marketplace.presentation.home.HomeScreen
import com.androstark.marketplace.presentation.list.AdListScreen
import com.androstark.marketplace.presentation.location.LocationPickerScreen
import com.androstark.marketplace.presentation.saved.SavedScreen
import com.androstark.marketplace.presentation.sell.SellScreen

@Composable
fun MarketplaceNavigation(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: BottomNavItem.Home.route

    Scaffold(
        bottomBar = {
            MarketplaceBottomNavigation(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    navController.navigate(route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen(
                    onNavigateToChat = {
                        navController.navigate("chat")
                    },
                    onNavigateToSell = {
                        navController.navigate(BottomNavItem.Sell.route)
                    },
                    onNavigateToProfile = {
                        navController.navigate(BottomNavItem.Profile.route)
                    },
                    onItemClick = { itemId ->
                        navController.navigate("ad_details/$itemId")
                    },
                    onCategoryClick = { category ->
                        navController.navigate("ad_list?category=${category.name}")
                    },
                    onViewAllClick = {
                        navController.navigate("ad_list")
                    }
                )
            }

            composable(BottomNavItem.Search.route) {
                // Placeholder for Search Screen
                SearchPlaceholderScreen()
            }

            composable(BottomNavItem.Sell.route) {
                SellScreen(
                    onBackClick = {
                        navController.navigateUp()
                    },
                    onNavigateToLocationPicker = {
                        navController.navigate("location_picker")
                    },
                    onNavigateToAdDetails = { adId ->
                        navController.navigate("ad_details/$adId") {
                            popUpTo(BottomNavItem.Home.route)
                        }
                    }
                )
            }

            // Location Picker Screen
            composable("location_picker") {
                LocationPickerScreen(
                    onBackClick = {
                        navController.navigateUp()
                    },
                    onLocationSelected = { locationData ->
                        // Pass location data back to SellScreen
                        // In a real implementation, you'd use a shared ViewModel or result API
                        navController.navigateUp()
                    }
                )
            }

            composable(BottomNavItem.Saved.route) {
                SavedScreen(
                    onItemClick = { itemId ->
                        navController.navigate("ad_details/$itemId")
                    }
                )
            }

            composable(BottomNavItem.Profile.route) {
                // Placeholder for Profile Screen
                ProfilePlaceholderScreen()
            }

            composable("chat") {
                ChatScreen(
                    onBackClick = {
                        navController.navigateUp()
                    },
                    onChatClick = { conversation ->
                        // Navigate to individual chat screen
                        // navController.navigate("chat_detail/${conversation.id}")
                    }
                )
            }

            composable(
                route = "ad_details/{itemId}",
                arguments = listOf(
                    navArgument("itemId") {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getInt("itemId") ?: 1
                val context = LocalContext.current
                
                AdDetailsScreen(
                    itemId = itemId,
                    onBackClick = {
                        navController.navigateUp()
                    },
                    onContactSeller = { chatItemId ->
                        navController.navigate("chat")
                    },
                    onShare = { title, text, imageUrl ->
                        shareContent(context, title, text, imageUrl)
                    }
                )
            }

            // Ad List Screen (for category browsing and view all)
            composable(
                route = "ad_list?category={category}",
                arguments = listOf(
                    navArgument("category") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )
            ) { backStackEntry ->
                val category = backStackEntry.arguments?.getString("category")
                
                AdListScreen(
                    category = category,
                    onBackClick = {
                        navController.navigateUp()
                    },
                    onItemClick = { itemId ->
                        navController.navigate("ad_details/$itemId")
                    }
                )
            }
        }
    }
}

@Composable
private fun SearchPlaceholderScreen() {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text(
            text = "Search Screen\nComing Soon",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SavedPlaceholderScreen() {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text(
            text = "Saved Items\nComing Soon",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ProfilePlaceholderScreen() {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text(
            text = "Profile Screen\nComing Soon",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
    }
}

private fun shareContent(context: Context, title: String, text: String, imageUrl: String) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, title)
        putExtra(Intent.EXTRA_TEXT, text)
    }
    
    val chooserIntent = Intent.createChooser(shareIntent, "Share via")
    context.startActivity(chooserIntent)
}
