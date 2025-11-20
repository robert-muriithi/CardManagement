package com.robert.network.datasource

import com.robert.network.api.CardApiService
import com.robert.network.model.RemoteCardDto
import com.robert.network.model.RemoteTransactionDto
import com.robert.network.model.RemoteUserProfileDto
import javax.inject.Inject


interface CardRemoteDataSource {
    suspend fun getCards(): List<RemoteCardDto>
    suspend fun getCardTransactions(): List<RemoteTransactionDto>
    suspend fun getUserProfile(): RemoteUserProfileDto
}

class CardRemoteDataSourceImpl @Inject constructor(
    private val api: CardApiService,
) : CardRemoteDataSource {

    override suspend fun getCards(): List<RemoteCardDto> =
        api.getCards().cards

    override suspend fun getCardTransactions(): List<RemoteTransactionDto> = api.getCardTransactions().transactions

    override suspend fun getUserProfile(): RemoteUserProfileDto =
        api.getUserProfile().user
}
