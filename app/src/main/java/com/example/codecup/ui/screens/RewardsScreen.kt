package com.example.codecup.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.codecup.data.RewardsRepository
import com.example.codecup.models.PointsHistoryItem
import com.example.codecup.ui.components.*
import com.example.codecup.ui.theme.*
import com.example.codecup.ui.viewmodels.RewardChoice
import com.example.codecup.ui.viewmodels.RewardsViewModel
import com.example.codecup.ui.viewmodels.ViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun RewardsScreen(
    onNavigate: (String) -> Unit,
    onRedeemClick: () -> Unit,
    viewModel: RewardsViewModel = viewModel(factory = ViewModelFactory(context = LocalContext.current))
) {
    val uiState by viewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    if (uiState.showRewardChoiceDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissDialog() },
            title = { Text("Loyalty Reward") },
            text = { Text("You've completed your stamps! What would you like to claim?") },
            confirmButton = {
                TextButton(onClick = { viewModel.claimReward(RewardChoice.POINTS) }) {
                    Text("500 Points")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.claimReward(RewardChoice.FREE_DRINK) }) {
                    Text("Free Drink (Now)")
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.primary,
            textContentColor = MaterialTheme.colorScheme.onSurface
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                AppDrawer(
                    currentRoute = "rewards",
                    onNavigate = onNavigate,
                    onCloseDrawer = { scope.launch { drawerState.close() } }
                )
            }
        ) {
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
                topBar = {
                    AppHeader(
                        title = "Rewards",
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        }
                    )
                },
                bottomBar = {
                    BottomNavBar(currentRoute = NavDestination.Rewards.route, onNavigate = onNavigate)
                },
                containerColor = MaterialTheme.colorScheme.background
            ) { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Loyalty Card
                    item {
                        LoyaltyCard(
                            stampsEarned = uiState.stamps,
                            onClick = {
                                if (uiState.stamps >= RewardsRepository.STAMPS_PER_CARD) {
                                    viewModel.onStampsCompleted()
                                } else {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            "Collect ${RewardsRepository.STAMPS_PER_CARD} stamps to get a free drink! " +
                                                "(${RewardsRepository.STAMPS_PER_CARD - uiState.stamps} left)"
                                        )
                                    }
                                }
                            }
                        )
                    }
                    
                    // Points Banner
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = MaterialTheme.colorScheme.surface,
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.size(48.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Stars,
                                        contentDescription = null,
                                        modifier = Modifier.padding(8.dp),
                                        tint = MaterialTheme.colorScheme.secondary
                                    )
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Total Points",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
                                    )
                                    Text(
                                        text = "${uiState.points} pts",
                                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onTertiaryContainer
                                    )
                                }
                                TextButton(onClick = onRedeemClick) {
                                    Text("Redeem", color = MaterialTheme.colorScheme.onTertiaryContainer, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                    
                    // Points History
                    item {
                        Text(
                            text = "Points History",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    
                    items(uiState.pointsHistory) { history ->
                        PointsHistoryRow(history)
                    }
                }
            }
        }

        ConfettiEffect(
            trigger = uiState.showCelebration,
            onAnimationEnd = { viewModel.dismissCelebration() }
        )
    }
}

@Composable
fun PointsHistoryRow(history: PointsHistoryItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = history.title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = history.date,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = history.points,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = if (history.isPositive) CoffeeStampGreen else MaterialTheme.colorScheme.secondary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RewardsPreview() {
    CodeCupTheme {
        RewardsScreen(onNavigate = {}, onRedeemClick = {})
    }
}
