package com.robert.coop_cardmanagement.navigation

object NavRoutes {
    const val CARDS = "cards"
    const val CARD_DETAILS = "card_details"
    const val BLOCK_CARD = "block_card"
    const val PROFILE_DETAILS = "profile_details"

    fun cardDetails(cardId: String) = "card_details/$cardId"

}