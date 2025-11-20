package com.robert.domain.models

data class Transaction(
    val id: String,
    val cardId: String,
    val amount: Double,
    val currency: String,
    val dateIso: String,
    val merchant: String,
    val type: WalletTransactionType,
)

enum class WalletTransactionType {
    DEBIT,
    CREDIT,
    PREPAID,
}