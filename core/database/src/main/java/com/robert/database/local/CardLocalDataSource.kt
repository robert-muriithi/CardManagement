package com.robert.database.local

import com.robert.database.dao.CardDao
import com.robert.database.dao.TransactionDao
import com.robert.database.dao.UserProfileDao
import com.robert.database.mappers.toDomain
import com.robert.database.mappers.toEntity
import com.robert.domain.models.PaymentCard
import com.robert.domain.models.Transaction
import com.robert.domain.models.UserProfile


interface CardLocalDataSource {
    suspend fun getCards(): List<PaymentCard>
    suspend fun saveCards(paymentCards: List<PaymentCard>)

    suspend fun getTransactions(cardId: String, limit: Int = 10): List<Transaction>
    suspend fun saveTransactions(cardId: String, transactions: List<Transaction>)

    suspend fun getUserProfile(): UserProfile?
    suspend fun saveUserProfile(profile: UserProfile)
}

class CardLocalDataSourceImpl(
    private val cardDao: CardDao,
    private val transactionDao: TransactionDao,
    private val userProfileDao: UserProfileDao,
) : CardLocalDataSource {

    override suspend fun getCards(): List<PaymentCard> =
        cardDao.getCardsWithWallets().map { cardWithWallets ->
            cardWithWallets.card.toDomain(
                wallets = cardWithWallets.wallets,
            )
        }

    override suspend fun saveCards(paymentCards: List<PaymentCard>) {
        val cardEntities = paymentCards.map { it.toEntity() }
        val walletEntities = paymentCards.flatMap { card ->
            card.wallets.orEmpty().map { wallet -> wallet.toEntity(cardId = card.id) }
        }

        cardDao.upsertCards(cardEntities)

        paymentCards.forEach { card ->
            cardDao.deleteWalletsForCard(card.id)
        }
        if (walletEntities.isNotEmpty()) {
            cardDao.upsertWallets(walletEntities)
        }
    }

    override suspend fun getTransactions(cardId: String, limit: Int): List<Transaction> =
        transactionDao.getTransactionsForCard(cardId = cardId, limit = limit)
            .map { it.toDomain() }

    override suspend fun saveTransactions(cardId: String, transactions: List<Transaction>) {
        val entities = transactions.map { it.toEntity(cardId = cardId) }
        transactionDao.upsertTransactions(entities)
    }

    override suspend fun getUserProfile(): UserProfile? =
        userProfileDao.getUserProfile()?.toDomain()

    override suspend fun saveUserProfile(profile: UserProfile) {
        userProfileDao.upsertUserProfile(profile.toEntity())
    }
}


