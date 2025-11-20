package com.robert.cards.utils

object CardUtils {

    fun maskCardNumber(cardNumber: String): String {
        return if (cardNumber.length >= 8) {
            val firstFour = cardNumber.take(4)
            val lastFour = cardNumber.takeLast(4)
            "$firstFour **** **** $lastFour"
        } else {
            cardNumber
        }
    }

    fun String.masked(): String {
        return maskCardNumber(this)
    }


}