package com.androstark.marketplace.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

data class ChatConversation(
    val id: String,
    val userName: String,
    val userAvatar: String,
    val lastMessage: String,
    val timestamp: String,
    val isOnline: Boolean,
    val unreadCount: Int,
    val itemTitle: String,
    val itemPrice: String,
    val itemImage: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    onBackClick: () -> Unit = {},
    onChatClick: (ChatConversation) -> Unit = {}
) {
    // Sample data - in real app this would come from ViewModel
    val conversations = remember {
        listOf(
            ChatConversation(
                id = "1",
                userName = "John Smith",
                userAvatar = "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=100",
                lastMessage = "Is this still available?",
                timestamp = "2m",
                isOnline = true,
                unreadCount = 2,
                itemTitle = "iPhone 14 Pro Max",
                itemPrice = "₹74,999",
                itemImage = "https://images.unsplash.com/photo-1592750475338-74b7b21085ab?w=100"
            ),
            ChatConversation(
                id = "2",
                userName = "Sarah Johnson",
                userAvatar = "https://images.unsplash.com/photo-1494790108755-2616b612b3c5?w=100",
                lastMessage = "Can we meet tomorrow?",
                timestamp = "1h",
                isOnline = false,
                unreadCount = 0,
                itemTitle = "Honda Civic 2018",
                itemPrice = "₹15,50,000",
                itemImage = "https://images.unsplash.com/photo-1552519507-da3b142c6e3d?w=100"
            ),
            ChatConversation(
                id = "3",
                userName = "Mike Wilson",
                userAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=100",
                lastMessage = "Thanks for the quick response!",
                timestamp = "3h",
                isOnline = true,
                unreadCount = 0,
                itemTitle = "MacBook Pro M2",
                itemPrice = "₹1,08,999",
                itemImage = "https://images.unsplash.com/photo-1541807084-5c52b6b3adef?w=100"
            ),
            ChatConversation(
                id = "4",
                userName = "Emma Davis",
                userAvatar = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=100",
                lastMessage = "What's the condition like?",
                timestamp = "1d",
                isOnline = false,
                unreadCount = 1,
                itemTitle = "Vintage Leather Sofa",
                itemPrice = "₹62,500",
                itemImage = "https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=100"
            )
        )
    }
    
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
                        text = "Messages",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "${conversations.count { it.unreadCount > 0 }} unread",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            actions = {
                IconButton(onClick = { /* Handle search */ }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                }
                IconButton(onClick = { /* Handle more options */ }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More options"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
        
        // Chat List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(conversations) { conversation ->
                ChatConversationItem(
                    conversation = conversation,
                    onClick = { onChatClick(conversation) }
                )
            }
        }
    }
}

@Composable
private fun ChatConversationItem(
    conversation: ChatConversation,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (conversation.unreadCount > 0) 
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
                else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // User Avatar with Online Status
            Box {
                AsyncImage(
                    model = conversation.userAvatar,
                    contentDescription = "${conversation.userName} avatar",
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                
                if (conversation.isOnline) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(Color(0xFF4CAF50), CircleShape)
                            .align(Alignment.BottomEnd)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Chat Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = conversation.userName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = if (conversation.unreadCount > 0) 
                                FontWeight.Bold else FontWeight.Medium
                        ),
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = conversation.timestamp,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        if (conversation.unreadCount > 0) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape,
                                modifier = Modifier.size(20.dp)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = conversation.unreadCount.toString(),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = conversation.lastMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (conversation.unreadCount > 0) 
                        MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Item Info
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = conversation.itemImage,
                        contentDescription = conversation.itemTitle,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(6.dp)),
                        contentScale = ContentScale.Crop
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Column {
                        Text(
                            text = conversation.itemTitle,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = conversation.itemPrice,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
            }
        }
    }
}
