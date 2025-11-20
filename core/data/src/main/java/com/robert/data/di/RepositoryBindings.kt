package com.robert.data.di

import com.robert.data.repository.CardsRepositoryImpl
import com.robert.domain.repository.CardRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryBindings {

    @Binds
    @Singleton
    abstract fun bindCardRepository(
        impl: CardsRepositoryImpl
    ): CardRepository
}