package com.androstark.marketplace.domain.repository

import com.androstark.marketplace.domain.model.ChatConversation
import com.androstark.marketplace.domain.model.ChatMessage

interface ChatRepository {
    
    suspend fun getConversations(): Result<List<ChatConversation>>
    
    suspend fun getConversation(conversationId: Int): Result<ChatConversation?>
    
    suspend fun getConversationByItemId(itemId: Int): Result<ChatConversation?>
    
    suspend fun sendMessage(conversationId: Int, message: String): Result<ChatMessage>
    
    suspend fun createConversation(itemId: Int, sellerId: Int, initialMessage: String): Result<ChatConversation>
}
