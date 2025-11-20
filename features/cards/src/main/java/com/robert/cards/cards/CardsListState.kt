package com.robert.cards.cards

import com.robert.domain.models.PaymentCard

data class CardsUIState(
    val isLoading: Boolean = false,
    val cards: List<PaymentCard> = emptyList(),
)