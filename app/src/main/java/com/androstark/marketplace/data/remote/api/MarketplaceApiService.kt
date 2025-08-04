package com.androstark.marketplace.data.remote.api

import com.androstark.marketplace.data.dto.CategoryDto
import com.androstark.marketplace.data.dto.ChatResponseDto
import com.androstark.marketplace.data.dto.ItemDetailsDto
import com.androstark.marketplace.data.dto.MarketplaceItemDto
import com.androstark.marketplace.data.remote.dto.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarketplaceApiService {
    
    @GET("categories")
    suspend fun getCategories(): Response<ApiResponse<List<CategoryDto>>>
    
    @GET("items/{id}")
    suspend fun getItemDetails(@Path("id") itemId: Int): ApiResponse<ItemDetailsDto>
    
    @GET("chat/conversations")
    suspend fun getConversations(): Response<ApiResponse<ChatResponseDto>>
    
    @GET("featured-items")
    suspend fun getFeaturedItems(): Response<ApiResponse<List<MarketplaceItemDto>>>
    
    @GET("items")
    suspend fun getItems(
        @Query("category") category: String? = null,
        @Query("search") search: String? = null,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<ApiResponse<List<MarketplaceItemDto>>>
}
