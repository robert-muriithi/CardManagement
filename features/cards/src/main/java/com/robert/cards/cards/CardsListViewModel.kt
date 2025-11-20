package com.robert.cards.cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robert.domain.common.Result
import com.robert.domain.models.CardType
import com.robert.domain.models.PaymentCard
import com.robert.domain.models.Wallet
import com.robert.domain.usecase.GetCardsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardsListViewModel @Inject constructor(
    private val getCardsUseCase: GetCardsUseCase,
) : ViewModel() {

    private val _uiState : MutableStateFlow<CardsUIState> = MutableStateFlow(CardsUIState())
    val uiState: StateFlow<CardsUIState> = _uiState.asStateFlow()

    private val _event = Channel<CardsListEvent>()
    val event = _event.receiveAsFlow()

    private val handler = CoroutineExceptionHandler { _, exception ->
        _uiState.update {
            it.copy(
                isLoading = false
            )
        }
        viewModelScope.launch {
            _event.send(CardsListEvent.Error(exception.message ?: "Unknown error"))
        }
    }

    init {
        fetchCards()
    }

    private fun fetchCards() {
        viewModelScope.launch(handler) {
            _uiState.update {
                it.copy(
                    isLoading = true
                )
            }
            val cards = getCardsUseCase()
            when (cards) {
                is Result.Data -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            cards = cards.value
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                    _event.send(CardsListEvent.Error(cards.message ?: "Unknown error"))
                }
                is Result.Loading -> {
                    _uiState.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                }
            }

        }
    }


    val sampleCards = listOf(
        PaymentCard(
            id = "card_001",
            userId = "user_12345",
            type = CardType.CREDIT,
            name = "Equity Visa Gold",
            cardNumber = "4111 1111 1111 1234",
            holderName = "Robert M. Njuguini",
            expiryDate = "2028-06",
            status = "ACTIVE",
            balance = 15200.75,
            currentSpend = 3200.50,
            creditLimit = 50000.00,
            dueDateIso = "2025-12-05T00:00:00Z",
            linkedAccountName = "Equity Personal Account",
            currency = "KES",
            wallets = listOf(
                Wallet(
                    currency = "KES",
                    flag = "🇰🇪",
                    balance = 15200.75
                ),
                Wallet(
                    currency = "USD",
                    flag = "🇺🇸",
                    balance = 120.50
                )
            )
        ),
        PaymentCard(
            id = "card_002",
            userId = "user_123454",
            type = CardType.CREDIT,
            name = "Equity Visa Gold",
            cardNumber = "4111 1111 1111 1234",
            holderName = "Robert M. Njuguini",
            expiryDate = "2028-06",
            status = "ACTIVE",
            balance = 15200.75,
            currentSpend = 3200.50,
            creditLimit = 50000.00,
            dueDateIso = "2025-12-05T00:00:00Z",
            linkedAccountName = "Equity Personal Account",
            currency = "KES",
            wallets = listOf(
                Wallet(
                    currency = "KES",
                    flag = "🇰🇪",
                    balance = 15200.75
                ),
                Wallet(
                    currency = "USD",
                    flag = "🇺🇸",
                    balance = 120.50
                )
            )
        )
    )


}

sealed class CardsListEvent {
    data class Error(val message: String) : CardsListEvent()
}