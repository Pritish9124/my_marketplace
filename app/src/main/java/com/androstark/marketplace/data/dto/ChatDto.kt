package com.androstark.marketplace.data.dto

import com.androstark.marketplace.domain.model.ChatConversation
import com.androstark.marketplace.domain.model.ChatMessage

data class ChatConversationDto(
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
    val messages: List<ChatMessageDto>
)

data class ChatMessageDto(
    val id: Int,
    val senderId: Int,
    val senderName: String,
    val message: String,
    val timestamp: String,
    val isFromCurrentUser: Boolean
)

data class ChatResponseDto(
    val conversations: List<ChatConversationDto>
)

// Extension functions for mapping
fun ChatConversationDto.toDomain(): ChatConversation {
    return ChatConversation(
        id = id,
        sellerId = sellerId,
        sellerName = sellerName,
        sellerAvatar = sellerAvatar,
        itemId = itemId,
        itemTitle = itemTitle,
        itemImage = itemImage,
        lastMessage = lastMessage,
        lastMessageTime = lastMessageTime,
        isRead = isRead,
        messages = messages.map { it.toDomain() }
    )
}

fun ChatMessageDto.toDomain(): ChatMessage {
    return ChatMessage(
        id = id,
        senderId = senderId,
        senderName = senderName,
        message = message,
        timestamp = timestamp,
        isFromCurrentUser = isFromCurrentUser
    )
}
