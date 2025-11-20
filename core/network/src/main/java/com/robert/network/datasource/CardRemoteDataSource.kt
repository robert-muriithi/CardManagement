package com.robert.network.datasource

import com.robert.network.api.CardApiService
import com.robert.network.model.RemoteCardDto
import com.robert.network.model.RemoteCardsResponse
import com.robert.network.model.RemoteTransactionDto
import com.robert.network.model.RemoteUserProfileDto
import com.robert.network.model.RemoteUserProfileResponse
import com.robert.network.sanitizer.MalformedCardsPayloadSanitizer
import com.robert.network.sanitizer.MalformedUserProfilePayloadSanitizer
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import kotlin.ExperimentalStdlibApi
import javax.inject.Inject
import kotlin.io.use


interface CardRemoteDataSource {
    suspend fun getCards(): List<RemoteCardDto>
    suspend fun getCardTransactions(): List<RemoteTransactionDto>
    suspend fun getUserProfile(): RemoteUserProfileDto
}

class CardRemoteDataSourceImpl @Inject constructor(
    private val api: CardApiService,
    private val moshi: Moshi,
    private val cardsPayloadSanitizer: MalformedCardsPayloadSanitizer,
    private val userPayloadSanitizer: MalformedUserProfilePayloadSanitizer,
) : CardRemoteDataSource {

    @OptIn(ExperimentalStdlibApi::class)
    private val cardsAdapter by lazy { moshi.adapter<RemoteCardsResponse>() }

    @OptIn(ExperimentalStdlibApi::class)
    private val userAdapter by lazy { moshi.adapter<RemoteUserProfileResponse>() }

    override suspend fun getCards(): List<RemoteCardDto> =
        api.getCards().use { body ->
            val normalizedPayload = cardsPayloadSanitizer.normalize(body.string())
            val parsedResponse = cardsAdapter.fromJson(normalizedPayload)
                ?: throw IllegalStateException("Empty cards payload")
            parsedResponse.cards
        }

    override suspend fun getCardTransactions(): List<RemoteTransactionDto> = api.getCardTransactions().transactions

    override suspend fun getUserProfile(): RemoteUserProfileDto =
        api.getUserProfile().use { body ->
            val normalizedPayload = userPayloadSanitizer.normalize(body.string())
            val parsedResponse = userAdapter.fromJson(normalizedPayload)
                ?: throw IllegalStateException("Empty user profile payload")
            parsedResponse.user
        }
}
