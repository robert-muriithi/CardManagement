package com.robert.cards.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.robert.cards.cards.components.BankCardItem
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CardDetailsScreen(
    cardId: String,
    onBackClick: () -> Unit,
    viewModel: CardDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(cardId) {
        viewModel.loadCardDetails(cardId)
    }

    val card = uiState.card
    val userProfile = uiState.userProfile

    if (uiState.isLoading && card == null) {
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(
                    top = 16.dp
                ),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (card != null) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                HeaderSection(
                    userName = userProfile?.firstName ?: "",
                    onBackClick = onBackClick,
                    onCloseClick = onBackClick
                )
            }
            item {
                CardBalanceSection(
                    balance = card.balance ?: 0.0,
                    currency = card.currency ?: "KES",
                    isVisible = uiState.isBalanceVisible,
                    onToggleVisibility = { viewModel.toggleBalanceVisibility() }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                BankCardItem(
                    card = card,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
            item {
                Text(
                    text = "Recent Transfers",
                    color = Color(0xFF006B54),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }

            items(uiState.transactions) { transaction ->
                TransactionItem(transaction = transaction)
            }
        }
    }
}

@Composable
private fun HeaderSection(
    userName: String,
    onBackClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF006B54))
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(
                    top = 16.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Text(
                text = "Hi $userName",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun CardBalanceSection(
    balance: Double,
    currency: String,
    isVisible: Boolean,
    onToggleVisibility: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "Card Balance",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = if (isVisible) {
                    "$currency ${NumberFormat.getNumberInstance(Locale.US).format(balance)}"
                } else {
                    "$currency ••••••"
                },
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(
                onClick = onToggleVisibility,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = if (isVisible) Icons.Default.Visibility else Icons.Outlined.VisibilityOff,
                    contentDescription = "Toggle visibility",
                    tint = Color.Gray
                )
            }
        }
    }
}



@Composable
private fun TransactionItem(transaction: com.robert.domain.models.Transaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFF0000)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = transaction.merchant.take(1).uppercase(),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Column {
                Text(
                    text = transaction.merchant,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = transaction.dateIso,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
        Text(
            text = "- ${NumberFormat.getCurrencyInstance(Locale.US).format(transaction.amount)} ${transaction.currency}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}

