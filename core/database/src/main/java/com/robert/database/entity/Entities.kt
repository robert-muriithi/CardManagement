package com.robert.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "cards")
data class CardEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val type: String,
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
)

@Entity(tableName = "wallets", primaryKeys = ["cardId", "currency"])
data class WalletEntity(
    val cardId: String,
    val currency: String,
    val flag: String,
    val balance: Double,
)

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey val id: String,
    val cardId: String,
    val amount: Double,
    val currency: String,
    val dateIso: String,
    val merchant: String,
    val type: String,
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val avatarUrl: String?,
    @Embedded val address: AddressEmbeddable,
)

data class AddressEmbeddable(
    val street: String,
    val city: String,
    val country: String,
    val postalCode: String,
)


data class CardWithWallets(
    @Embedded val card: CardEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "cardId",
    )
    val wallets: List<WalletEntity>,
)
