package com.androstark.marketplace.data.repository

import com.androstark.marketplace.data.dto.ChatConversationDto
import com.androstark.marketplace.data.dto.ChatMessageDto
import com.androstark.marketplace.data.dto.toDomain
import com.androstark.marketplace.data.remote.api.MarketplaceApiService
import com.androstark.marketplace.domain.model.ChatConversation
import com.androstark.marketplace.domain.model.ChatMessage
import com.androstark.marketplace.domain.repository.ChatRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val apiService: MarketplaceApiService
) : ChatRepository {

    // Cache for conversations - in real app this would be handled by proper caching mechanism
    private var conversationsCache: List<ChatConversationDto>? = null

    override suspend fun getConversations(): Result<List<ChatConversation>> {
        return try {
            if (conversationsCache == null) {
                val response = apiService.getConversations()
                if (response.isSuccessful && response.body()?.success == true) {
                    conversationsCache = response.body()?.data?.conversations ?: emptyList()
                } else {
                    return Result.failure(Exception("Failed to load conversations"))
                }
            }
            Result.success(conversationsCache?.map { it.toDomain() } ?: emptyList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getConversation(conversationId: Int): Result<ChatConversation?> {
        return try {
            // First ensure we have conversations loaded
            getConversations()
            val conversation = conversationsCache?.find { it.id == conversationId }
            Result.success(conversation?.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getConversationByItemId(itemId: Int): Result<ChatConversation?> {
        return try {
            // First ensure we have conversations loaded
            getConversations()
            val conversation = conversationsCache?.find { it.itemId == itemId }
            Result.success(conversation?.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendMessage(conversationId: Int, message: String): Result<ChatMessage> {
        return try {
            delay(200) // Simulate network delay
            // In a real app, this would make an API call to send the message
            // For now, we'll simulate adding to local cache
            val conversation = conversationsCache?.find { it.id == conversationId }
            if (conversation != null) {
                val newMessage = ChatMessageDto(
                    id = conversation.messages.size + 1,
                    senderId = 0,
                    senderName = "You",
                    message = message,
                    timestamp = "Now",
                    isFromCurrentUser = true
                )
                // In real app, this would be handled by the API
                Result.success(newMessage.toDomain())
            } else {
                Result.failure(Exception("Conversation not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createConversation(itemId: Int, sellerId: Int, initialMessage: String): Result<ChatConversation> {
        return try {
            delay(400) // Simulate network delay
            
            // First ensure we have conversations loaded
            getConversations()
            
            // Check if conversation already exists
            val existingConversation = conversationsCache?.find { it.itemId == itemId }
            if (existingConversation != null) {
                return Result.success(existingConversation.toDomain())
            }
            
            // In a real app, this would make an API call to create a new conversation
            // For now, we'll simulate success since the data comes from JSON
            // The actual conversation creation would be handled by the backend
            
            // Return a mock success - in real app this would be the newly created conversation from API
            val mockNewConversation = ChatConversationDto(
                id = (conversationsCache?.size ?: 0) + 1,
                sellerId = sellerId,
                sellerName = "New Seller", // In real app, this would come from API
                sellerAvatar = "",
                itemId = itemId,
                itemTitle = "Item $itemId", // In real app, this would come from API
                itemImage = "",
                lastMessage = initialMessage,
                lastMessageTime = "Now",
                isRead = false,
                messages = listOf(
                    ChatMessageDto(
                        id = 1,
                        senderId = 0,
                        senderName = "You",
                        message = initialMessage,
                        timestamp = "Now",
                        isFromCurrentUser = true
                    )
                )
            )
            
            Result.success(mockNewConversation.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
