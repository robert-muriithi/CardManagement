package com.robert.network.mapper

import com.robert.domain.models.Address
import com.robert.domain.models.PaymentCard
import com.robert.domain.models.CardType
import com.robert.domain.models.Transaction
import com.robert.domain.models.UserProfile
import com.robert.domain.models.Wallet
import com.robert.domain.models.WalletTransactionType
import com.robert.network.model.RemoteAddressDto
import com.robert.network.model.RemoteCardDto
import com.robert.network.model.RemoteCardType
import com.robert.network.model.RemoteTransactionDto
import com.robert.network.model.RemoteTransactionType
import com.robert.network.model.RemoteUserProfileDto
import com.robert.network.model.RemoteWalletDto

fun RemoteCardDto.toDomain(): PaymentCard =
    PaymentCard(
        id = id,
        userId = userId,
        type = type?.toDomain(),
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
        wallets = wallets?.map { it.toDomain() },
    )

fun RemoteCardType.toDomain(): CardType =
    when (this) {
        RemoteCardType.PREPAID -> CardType.PREPAID
        RemoteCardType.CREDIT -> CardType.CREDIT
        RemoteCardType.MULTI_CURRENCY -> CardType.MULTI_CURRENCY
        RemoteCardType.DEBIT -> CardType.DEBIT
    }

fun RemoteWalletDto.toDomain(): Wallet =
    Wallet(
        currency = currency,
        flag = flag,
        balance = balance,
    )

fun RemoteTransactionDto.toDomain(): Transaction =
    Transaction(
        id = id,
        cardId = cardId,
        amount = amount,
        currency = currency,
        dateIso = dateIso,
        merchant = merchant,
        type = type.toDomain(),
    )

fun RemoteTransactionType.toDomain(): WalletTransactionType =
    when (this) {
        RemoteTransactionType.DEBIT -> WalletTransactionType.DEBIT
        RemoteTransactionType.CREDIT -> WalletTransactionType.CREDIT
        RemoteTransactionType.PREPAID -> WalletTransactionType.PREPAID
    }

fun RemoteUserProfileDto.toDomain(): UserProfile =
    UserProfile(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        phone = phone,
        avatarUrl = avatarUrl,
        address = address.toDomain(),
    )

fun RemoteAddressDto.toDomain(): Address =
    Address(
        street = street,
        city = city,
        country = country,
        postalCode = postalCode,
    )
