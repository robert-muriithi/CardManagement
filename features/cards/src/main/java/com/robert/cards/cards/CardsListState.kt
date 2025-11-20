package com.robert.cards.cards

import com.robert.domain.models.PaymentCard
import com.robert.domain.models.UserProfile

data class CardsUIState(
    val isLoading: Boolean = false,
    val cards: List<PaymentCard> = emptyList(),
    val userProfile: UserProfile? = null
)