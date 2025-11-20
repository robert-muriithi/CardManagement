package com.robert.domain.models

data class PaymentCard(
    val id: String,
    val userId: String,
    val type: CardType? = null,
    val name: String,
    val cardNumber: String,
    val holderName: String,
    val expiryDate: String,
    val status: String,
    val balance: Double?,
    val currentSpend: Double?,
    val creditLimit: Double?,
    val dueDateIso: String?,
    val linkedAccountName: String?,
    val currency: String?,
    val wallets: List<Wallet>?,
)

enum class CardType {
    PREPAID,
    CREDIT,
    MULTI_CURRENCY,
    DEBIT,
}