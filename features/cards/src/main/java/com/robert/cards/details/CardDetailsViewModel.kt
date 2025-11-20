package com.robert.cards.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robert.domain.common.Result
import com.robert.domain.models.PaymentCard
import com.robert.domain.models.Transaction
import com.robert.domain.models.UserProfile
import com.robert.domain.usecase.GetCardTransactionsUseCase
import com.robert.domain.usecase.GetCardsUseCase
import com.robert.domain.usecase.GetUserProfileUseCase
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
class CardDetailsViewModel @Inject constructor(
    private val getCardsUseCase: GetCardsUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getCardTransactionsUseCase: GetCardTransactionsUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<CardDetailsUIState> = MutableStateFlow(CardDetailsUIState())
    val uiState: StateFlow<CardDetailsUIState> = _uiState.asStateFlow()

    private val _event = Channel<CardDetailsEvent>()
    val event = _event.receiveAsFlow()

    private val handler = CoroutineExceptionHandler { _, exception ->
        _uiState.update {
            it.copy(isLoading = false)
        }
        viewModelScope.launch {
            _event.send(CardDetailsEvent.Error(exception.message ?: "Unknown error"))
        }
    }

    fun loadCardDetails(cardId: String) {
        viewModelScope.launch(handler) {
            _uiState.update { it.copy(isLoading = true) }

            val cardsResult = getCardsUseCase()
            when (cardsResult) {
                is Result.Data -> {
                    val card = cardsResult.value.find { it.id == cardId }
                    if (card != null) {
                        _uiState.update {
                            it.copy(
                                card = card,
                                isLoading = false
                            )
                        }
                        fetchTransactions(cardId)
                    } else {
                        _event.send(CardDetailsEvent.Error("Card not found"))
                        _uiState.update { it.copy(isLoading = false) }
                    }
                }
                is Result.Error -> {
                    _event.send(CardDetailsEvent.Error(cardsResult.message ?: "Failed to load card"))
                    _uiState.update { it.copy(isLoading = false) }
                }
                is Result.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }

            val userResult = getUserProfileUseCase()
            when (userResult) {
                is Result.Data -> {
                    _uiState.update {
                        it.copy(userProfile = userResult.value)
                    }
                }
                is Result.Error -> {}
                is Result.Loading -> {}
            }
        }
    }

    private fun fetchTransactions(cardId: String) {
        viewModelScope.launch(handler) {
            val transactionsResult = getCardTransactionsUseCase(cardId, limit = 10)
            when (transactionsResult) {
                is Result.Data -> {
                    _uiState.update {
                        it.copy(transactions = transactionsResult.value)
                    }
                }
                is Result.Error -> {}
                is Result.Loading -> {}
            }
        }
    }

    fun toggleBalanceVisibility() {
        _uiState.update {
            it.copy(isBalanceVisible = !it.isBalanceVisible)
        }
    }
}

data class CardDetailsUIState(
    val isLoading: Boolean = false,
    val card: PaymentCard? = null,
    val userProfile: UserProfile? = null,
    val transactions: List<Transaction> = emptyList(),
    val isBalanceVisible: Boolean = true,
)

sealed class CardDetailsEvent {
    data class Error(val message: String) : CardDetailsEvent()
}

