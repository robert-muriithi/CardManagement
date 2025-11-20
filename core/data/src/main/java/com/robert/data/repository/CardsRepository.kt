package com.robert.data.repository


import android.util.Log
import com.robert.database.local.CardLocalDataSource
import com.robert.domain.common.Result
import com.robert.domain.models.PaymentCard
import com.robert.domain.models.Transaction
import com.robert.domain.models.UserProfile
import com.robert.domain.repository.CardRepository
import com.robert.network.datasource.CardRemoteDataSource
import com.robert.network.mapper.toDomain
import com.robert.network.model.RemoteCardsResponse
import javax.inject.Inject


class CardsRepositoryImpl @Inject constructor(
    private val remoteDataSource: CardRemoteDataSource,
    private val localDataSource: CardLocalDataSource,
) : CardRepository {

    override suspend fun getCards(): Result<List<PaymentCard>> {
        return try {
//            val moshi =
//            val jsonString = response.body?.string()
//            val actualJson = jsonString?.removeSurrounding("\"")?.replace("\\\"", "\"")
//            val cardsResponse = moshi.adapter<RemoteCardsResponse>().fromJson(actualJson)
            val localCards = runCatching { localDataSource.getCards() }.getOrElse {
                emptyList()
            }

            try {
                val remoteCards = remoteDataSource.getCards().map { it.toDomain() }
                localDataSource.saveCards(remoteCards)

                Result.Data(remoteCards)
            } catch (e: Exception) {
                if (localCards.isNotEmpty()) {
                    Result.Data(localCards)
                } else {
                    Result.Error(message = e.message ?: "Failed to fetch cards", throwable = e)
                }
            }
        } catch (e: Exception) {
            Result.Error(message = e.message ?: "Unexpected error", throwable = e)
        }
    }

    override suspend fun getTransactions(cardId: String, limit: Int): Result<List<Transaction>> {
        return try {
            val localTransactions = runCatching {
                localDataSource.getTransactions(cardId = cardId, limit = limit)
            }.getOrElse {
                emptyList()
            }

            try {
                val remoteTransactions = remoteDataSource.getCardTransactions()
                    .map { it.toDomain() }

                localDataSource.saveTransactions(cardId = cardId, transactions = remoteTransactions)

                Result.Data(remoteTransactions)
            } catch (e: Exception) {
                if (localTransactions.isNotEmpty()) {
                    Result.Data(localTransactions)
                } else {
                    Result.Error(message = e.message ?: "Failed to fetch transactions", throwable = e)
                }
            }
        } catch (e: Exception) {
            Result.Error(message = e.message ?: "Unexpected error", throwable = e)
        }
    }

    override suspend fun getUserProfile(): Result<UserProfile> {
        return try {
            val localProfile = runCatching {
                localDataSource.getUserProfile()
            }.getOrElse {
                null
            }
            try {
                val remoteProfile = remoteDataSource.getUserProfile().toDomain()
                localDataSource.saveUserProfile(remoteProfile)
                Result.Data(remoteProfile)
            } catch (e: Exception) {

                localProfile?.let {
                    Result.Data(it)
                } ?: Result.Error(message = e.message ?: "Failed to fetch profile", throwable = e)
            }
        } catch (e: Exception) {
            Result.Error(message = e.message ?: "Unexpected error", throwable = e)
        }
    }
}