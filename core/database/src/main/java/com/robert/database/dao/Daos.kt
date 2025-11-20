package com.robert.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.robert.database.entity.CardEntity
import com.robert.database.entity.TransactionEntity
import com.robert.database.entity.UserProfileEntity
import com.robert.database.entity.WalletEntity
import com.robert.database.entity.CardWithWallets

@Dao
interface CardDao {

    @Transaction
    @Query("SELECT * FROM cards")
    suspend fun getCardsWithWallets(): List<CardWithWallets>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCards(cards: List<CardEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertWallets(wallets: List<WalletEntity>)

    @Query("DELETE FROM wallets WHERE cardId = :cardId")
    suspend fun deleteWalletsForCard(cardId: String)
}

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions WHERE cardId = :cardId ORDER BY dateIso DESC LIMIT :limit")
    suspend fun getTransactionsForCard(cardId: String, limit: Int): List<TransactionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTransactions(transactions: List<TransactionEntity>)
}

@Dao
interface UserProfileDao {

    @Query("SELECT * FROM user_profile LIMIT 1")
    suspend fun getUserProfile(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUserProfile(profile: UserProfileEntity)
}
