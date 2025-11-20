package com.robert.cards.cards

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robert.domain.common.Result
import com.robert.domain.models.CardType
import com.robert.domain.models.PaymentCard
import com.robert.domain.models.Wallet
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
class CardsListViewModel @Inject constructor(
    private val getCardsUseCase: GetCardsUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<CardsUIState> = MutableStateFlow(CardsUIState())
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
        fetchUserData()
    }

    private fun fetchUserData() {
        viewModelScope.launch(handler) {
            _uiState.update {
                it.copy(
                    isLoading = true
                )
            }

            val userDetails = getUserProfileUseCase()
            when (userDetails) {
                is Result.Data -> {
                    Log.d("TAG", "fetchUserData Success: ${userDetails.value}")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            userProfile = userDetails.value
                        )
                    }
                }

                is Result.Error -> {
                    Log.d("TAG", "fetchUserData Failure: ${userDetails.message}")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                    _event.send(CardsListEvent.Error(userDetails.message ?: "Unknown error"))
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


}

sealed class CardsListEvent {
    data class Error(val message: String) : CardsListEvent()
}