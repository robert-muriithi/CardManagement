package com.robert.database

import android.content.Context
import androidx.room.Room
import com.robert.database.dao.CardDao
import com.robert.database.dao.TransactionDao
import com.robert.database.dao.UserProfileDao
import com.robert.database.local.CardLocalDataSource
import com.robert.database.local.CardLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "coop_card_management.db",
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideCardDao(db: AppDatabase): CardDao = db.cardDao()

    @Provides
    fun provideTransactionDao(db: AppDatabase): TransactionDao = db.transactionDao()

    @Provides
    fun provideUserProfileDao(db: AppDatabase): UserProfileDao = db.userProfileDao()

    @Provides
    @Singleton
    fun provideCardLocalDataSource(
        cardDao: CardDao,
        transactionDao: TransactionDao,
        userProfileDao: UserProfileDao,
    ): CardLocalDataSource = CardLocalDataSourceImpl(
        cardDao = cardDao,
        transactionDao = transactionDao,
        userProfileDao = userProfileDao,
    )
}
