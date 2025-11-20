package com.robert.network.api

import com.robert.network.model.RemoteTransactionsResponse
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CardApiService {
    @GET("getCards")
    suspend fun getCards(): ResponseBody

    @GET("cardTransactions")
    suspend fun getCardTransactions(): RemoteTransactionsResponse

    @GET("userProfile")
    suspend fun getUserProfile(): ResponseBody
}
