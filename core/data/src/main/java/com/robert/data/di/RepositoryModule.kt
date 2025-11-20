package com.robert.data.di

import com.robert.data.repository.CardsRepositoryImpl
import com.robert.database.local.CardLocalDataSource
import com.robert.domain.repository.CardRepository
import com.robert.network.datasource.CardRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


//@Module
//@InstallIn(SingletonComponent::class)
//object RepositoryModule {
//
//    @Provides
//    @Singleton
//    fun provideRepositpry(
//        remoteDataSource: CardRemoteDataSource,
//        localDataSource: CardLocalDataSource,
//    ): CardRepository = CardsRepositoryImpl(
//        remoteDataSource = remoteDataSource,
//        localDataSource = localDataSource,
//    )
//}