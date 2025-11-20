package com.robert.coop_cardmanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.robert.cards.cards.CardsListScreen
import com.robert.cards.details.CardDetailsScreen
import com.robert.coop_cardmanagement.navigation.NavRoutes
import com.robert.coop_cardmanagement.ui.theme.CoopCardManagementTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoopCardManagementTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = NavRoutes.CARDS
                ) {
                    composable(NavRoutes.CARDS) {
                        CardsListScreen(
                            onCardClick = { cardId ->
                                navController.navigate(NavRoutes.cardDetails(cardId))
                            }
                        )
                    }
                    composable(
                        route = "${NavRoutes.CARD_DETAILS}/{cardId}",
                        arguments = listOf(
                            navArgument("cardId") {
                                type = NavType.StringType
                            }
                        )
                    ) { backStackEntry ->
                        val cardId = backStackEntry.arguments?.getString("cardId") ?: ""
                        CardDetailsScreen(
                            cardId = cardId,
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}

