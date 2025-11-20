package com.robert.database.mappers

import com.robert.database.entity.AddressEmbeddable
import com.robert.database.entity.CardEntity
import com.robert.database.entity.TransactionEntity
import com.robert.database.entity.UserProfileEntity
import com.robert.database.entity.WalletEntity
import com.robert.domain.models.Address
import com.robert.domain.models.PaymentCard
import com.robert.domain.models.CardType
import com.robert.domain.models.Transaction
import com.robert.domain.models.UserProfile
import com.robert.domain.models.Wallet
import com.robert.domain.models.WalletTransactionType

fun CardEntity.toDomain(wallets: List<WalletEntity>): PaymentCard =
    PaymentCard(
        id = id,
        userId = userId,
        type = when (type) {
            CardType.PREPAID.name -> CardType.PREPAID
            CardType.CREDIT.name -> CardType.CREDIT
            CardType.MULTI_CURRENCY.name -> CardType.MULTI_CURRENCY
            CardType.DEBIT.name -> CardType.DEBIT
            else -> CardType.PREPAID
        },
        name = name,
        cardNumber = cardNumber,
        holderName = holderName,
        expiryDate = expiryDate,
        status = status,
        balance = balance,
        currentSpend = currentSpend,
        creditLimit = creditLimit,
        dueDateIso = dueDateIso,
        linkedAccountName = linkedAccountName,
        currency = currency,
        wallets = wallets.map { it.toDomain() },
    )
 fun PaymentCard.toEntity(): CardEntity =
    CardEntity(
        id = id,
        userId = userId,
        type = type?.name.orEmpty(),
        name = name,
        cardNumber = cardNumber,
        holderName = holderName,
        expiryDate = expiryDate,
        status = status,
        balance = balance,
        currentSpend = currentSpend,
        creditLimit = creditLimit,
        dueDateIso = dueDateIso,
        linkedAccountName = linkedAccountName,
        currency = currency,
    )

 fun WalletEntity.toDomain(): Wallet =
    Wallet(
        currency = currency,
        flag = flag,
        balance = balance,
    )

 fun Wallet.toEntity(cardId: String): WalletEntity =
    WalletEntity(
        cardId = cardId,
        currency = currency,
        flag = flag,
        balance = balance,
    )

 fun TransactionEntity.toDomain(): Transaction =
    Transaction(
        id = id,
        cardId = cardId,
        amount = amount,
        currency = currency,
        dateIso = dateIso,
        merchant = merchant,
        type = when (type) {
            WalletTransactionType.DEBIT.name -> WalletTransactionType.DEBIT
            WalletTransactionType.CREDIT.name -> WalletTransactionType.CREDIT
            WalletTransactionType.PREPAID.name -> WalletTransactionType.PREPAID
            else -> WalletTransactionType.DEBIT
        },
    )

 fun Transaction.toEntity(cardId: String): TransactionEntity =
    TransactionEntity(
        id = id,
        cardId = cardId,
        amount = amount,
        currency = currency,
        dateIso = dateIso,
        merchant = merchant,
        type = type.name,
    )

 fun UserProfileEntity.toDomain(): UserProfile =
    UserProfile(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        phone = phone,
        avatarUrl = avatarUrl,
        address = Address(
            street = address.street,
            city = address.city,
            country = address.country,
            postalCode = address.postalCode,
        ),
    )

 fun UserProfile.toEntity(): UserProfileEntity =
    UserProfileEntity(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        phone = phone,
        avatarUrl = avatarUrl,
        address = AddressEmbeddable(
            street = address.street,
            city = address.city,
            country = address.country,
            postalCode = address.postalCode,
        ),
    )