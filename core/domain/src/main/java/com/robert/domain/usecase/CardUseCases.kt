package com.robert.domain.usecase

import com.robert.domain.common.Result
import com.robert.domain.models.PaymentCard
import com.robert.domain.models.Transaction
import com.robert.domain.models.UserProfile
import com.robert.domain.repository.CardRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCardsUseCase @Inject constructor(
    private val repository: CardRepository,
) {
    suspend operator fun invoke(): Result<List<PaymentCard>> = repository.getCards()
}

@Singleton
class GetCardTransactionsUseCase @Inject constructor(
    private val repository: CardRepository,
) {
    suspend operator fun invoke(cardId: String, limit: Int = 10): Result<List<Transaction>> =
        repository.getTransactions(cardId = cardId, limit = limit)
}

@Singleton
class GetUserProfileUseCase @Inject constructor(
    private val repository: CardRepository,
) {
    suspend operator fun invoke(): Result<UserProfile> = repository.getUserProfile()
}
