package com.robert.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


enum class RemoteCardType {
    @Json(name = "PREPAID")
    PREPAID,

    @Json(name = "CREDIT")
    CREDIT,

    @Json(name = "MULTI_CURRENCY")
    MULTI_CURRENCY,

    @Json(name = "DEBIT")
    DEBIT,
}

@JsonClass(generateAdapter = true)
data class RemoteCardsResponse(
    @Json(name = "cards") val cards: List<RemoteCardDto>,
)

@JsonClass(generateAdapter = true)
data class RemoteCardDto(
    @Json(name = "id") val id: String,
    @Json(name = "userId") val userId: String,
    @Json(name = "type") val type: RemoteCardType? = null,
    @Json(name = "name") val name: String,
    @Json(name = "cardNumber") val cardNumber: String,
    @Json(name = "holderName") val holderName: String,
    @Json(name = "expiryDate") val expiryDate: String,
    @Json(name = "status") val status: String,
    @Json(name = "balance") val balance: Double?,
    @Json(name = "currentSpend") val currentSpend: Double?,
    @Json(name = "creditLimit") val creditLimit: Double?,
    @Json(name = "dueDate") val dueDateIso: String?,
    @Json(name = "linkedAccountName") val linkedAccountName: String?,
    @Json(name = "currency") val currency: String?,
    @Json(name = "wallets") val wallets: List<RemoteWalletDto>?,
)

data class RemoteWalletDto(
    @Json(name = "currency") val currency: String,
    @Json(name = "flag") val flag: String,
    @Json(name = "balance") val balance: Double,
)


enum class RemoteTransactionType {
    @Json(name = "DEBIT")
    DEBIT,

    @Json(name = "CREDIT")
    CREDIT,

    @Json(name = "PREPAID")
    PREPAID,
}


data class RemoteTransactionsResponse(
    @Json(name = "transactions") val transactions: List<RemoteTransactionDto>,
)


data class RemoteTransactionDto(
    @Json(name = "id") val id: String,
    @Json(name = "cardId") val cardId: String,
    @Json(name = "amount") val amount: Double,
    @Json(name = "currency") val currency: String,
    @Json(name = "date") val dateIso: String,
    @Json(name = "merchant") val merchant: String,
    @Json(name = "type") val type: RemoteTransactionType,
)


data class RemoteUserProfileResponse(
    @Json(name = "user") val user: RemoteUserProfileDto,
)
data class RemoteUserProfileDto(
    @Json(name = "id") val id: String,
    @Json(name = "firstName") val firstName: String,
    @Json(name = "lastName") val lastName: String,
    @Json(name = "email") val email: String,
    @Json(name = "phone") val phone: String,
    @Json(name = "avatarUrl") val avatarUrl: String?,
    @Json(name = "address") val address: RemoteAddressDto,
)

data class RemoteAddressDto(
    @Json(name = "street") val street: String,
    @Json(name = "city") val city: String,
    @Json(name = "country") val country: String,
    @Json(name = "postalCode") val postalCode: String,
)
