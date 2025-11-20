package com.robert.network.api

import com.robert.network.model.RemoteCardsResponse
import com.robert.network.model.RemoteTransactionsResponse
import com.robert.network.model.RemoteUserProfileResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CardApiService {
    @GET("getCards")
    suspend fun getCards(): RemoteCardsResponse

    @GET("cardTransactions")
    suspend fun getCardTransactions(): RemoteTransactionsResponse

    @GET("getUser")
    suspend fun getUserProfile(): RemoteUserProfileResponse
}
