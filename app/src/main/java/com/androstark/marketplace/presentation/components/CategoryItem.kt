package com.androstark.marketplace.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.androstark.marketplace.domain.model.Category

@Composable
fun CategoryItem(
    category: Category,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = getCategoryBackgroundColor(category.name)
    val iconColor = getCategoryIconColor(category.name)
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(backgroundColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = getCategoryIcon(category.name),
                contentDescription = category.name,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = category.name,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Medium
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        
        Text(
            text = "${category.adCount} ads",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun getCategoryBackgroundColor(categoryName: String): Color {
    return when (categoryName) {
        "Vehicles" -> Color(0xFFE3F2FD)
        "Property" -> Color(0xFFE8F5E8)
        "Electronics" -> Color(0xFFF3E5F5)
        "Fashion" -> Color(0xFFFFF3E0)
        "Furniture" -> Color(0xFFFFEBEE)
        "Cameras" -> Color(0xFFE0F2F1)
        "Gaming" -> Color(0xFFFCE4EC)
        "Books" -> Color(0xFFF1F8E9)
        else -> Color(0xFFF5F5F5)
    }
}

private fun getCategoryIconColor(categoryName: String): Color {
    return when (categoryName) {
        "Vehicles" -> Color(0xFF1976D2)
        "Property" -> Color(0xFF388E3C)
        "Electronics" -> Color(0xFF7B1FA2)
        "Fashion" -> Color(0xFFF57C00)
        "Furniture" -> Color(0xFFD32F2F)
        "Cameras" -> Color(0xFF00796B)
        "Gaming" -> Color(0xFFC2185B)
        "Books" -> Color(0xFF689F38)
        else -> Color(0xFF757575)
    }
}

private fun getCategoryIcon(categoryName: String): ImageVector {
    return when (categoryName) {
        "Vehicles" -> Icons.Default.DirectionsCar
        "Property" -> Icons.Default.Home
        "Electronics" -> Icons.Default.PhoneAndroid
        "Fashion" -> Icons.Default.Checkroom
        "Furniture" -> Icons.Default.Chair
        "Cameras" -> Icons.Default.CameraAlt
        "Gaming" -> Icons.Default.SportsEsports
        "Books" -> Icons.Default.MenuBook
        else -> Icons.Default.Category
    }
}
