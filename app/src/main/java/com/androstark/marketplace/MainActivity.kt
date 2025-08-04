package com.androstark.marketplace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.androstark.marketplace.presentation.navigation.MarketplaceNavigation
import com.androstark.marketplace.ui.theme.MyMarketplaceTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyMarketplaceTheme {
                MarketplaceNavigation()
            }
        }
    }
}