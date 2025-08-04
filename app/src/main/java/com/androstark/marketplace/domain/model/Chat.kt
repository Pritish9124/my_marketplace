package com.androstark.marketplace.domain.model

data class ChatConversation(
    val id: Int,
    val sellerId: Int,
    val sellerName: String,
    val sellerAvatar: String,
    val itemId: Int,
    val itemTitle: String,
    val itemImage: String,
    val lastMessage: String,
    val lastMessageTime: String,
    val isRead: Boolean,
    val messages: List<ChatMessage>
)

data class ChatMessage(
    val id: Int,
    val senderId: Int,
    val senderName: String,
    val message: String,
    val timestamp: String,
    val isFromCurrentUser: Boolean
)
