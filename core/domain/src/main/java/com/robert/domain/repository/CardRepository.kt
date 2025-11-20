package com.robert.domain.repository

import com.robert.domain.common.Result
import com.robert.domain.models.PaymentCard
import com.robert.domain.models.Transaction
import com.robert.domain.models.UserProfile

interface CardRepository {
    suspend fun getCards(): Result<List<PaymentCard>>
    suspend fun getTransactions(cardId: String, limit: Int = 10): Result<List<Transaction>>
    suspend fun getUserProfile(): Result<UserProfile>
}
