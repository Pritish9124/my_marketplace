package com.androstark.marketplace.di

import com.androstark.marketplace.data.repository.ChatRepositoryImpl
import com.androstark.marketplace.data.repository.MarketplaceRepositoryImpl
import com.androstark.marketplace.domain.repository.ChatRepository
import com.androstark.marketplace.domain.repository.MarketplaceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMarketplaceRepository(
        marketplaceRepositoryImpl: MarketplaceRepositoryImpl
    ): MarketplaceRepository

    @Binds
    @Singleton
    abstract fun bindChatRepository(
        chatRepositoryImpl: ChatRepositoryImpl
    ): ChatRepository
}
