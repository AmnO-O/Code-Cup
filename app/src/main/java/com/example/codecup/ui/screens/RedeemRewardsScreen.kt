package com.example.codecup.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.codecup.data.RewardsRepository
import com.example.codecup.models.Product
import com.example.codecup.ui.components.AppHeader
import com.example.codecup.ui.components.ConfettiEffect
import com.example.codecup.ui.components.PrimaryButton
import com.example.codecup.ui.theme.*
import com.example.codecup.ui.viewmodels.RedeemRewardsViewModel
import com.example.codecup.ui.viewmodels.ViewModelFactory

@Composable
fun RedeemRewardsScreen(
    onBackClick: () -> Unit,
    viewModel: RedeemRewardsViewModel = viewModel(factory = ViewModelFactory(context = LocalContext.current))
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(uiState.redeemSuccess) {
        if (uiState.redeemSuccess) {
            // Potentially show a snackbar or navigate back
            viewModel.resetSuccess()
        }
    }

    if (uiState.showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissDialog() },
            title = { Text("Redeem Reward") },
            text = { Text("Do you want to order this drink now?") },
            confirmButton = {
                TextButton(onClick = { viewModel.confirmRedeem(takeNow = true) }) {
                    Text("Order Now")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.confirmRedeem(takeNow = false) }) {
                    Text("Cancel")
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.primary,
            textContentColor = MaterialTheme.colorScheme.onSurface
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                AppHeader(title = "Redeem Rewards", onBackClick = onBackClick)
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Balance Banner
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.tertiaryContainer
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "You have ",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${uiState.pointsBalance} points",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
                
                items(uiState.products) { product ->
                    val pointCost = RewardsRepository.redeemCostFor(product.price)
                    val canRedeem = uiState.pointsBalance >= pointCost
                    
                    RedeemItemRow(
                        product = product,
                        pointCost = pointCost,
                        canRedeem = canRedeem,
                        pointsBalance = uiState.pointsBalance,
                        onRedeemClick = { viewModel.initiateRedeem(product) }
                    )
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
fun RedeemItemRow(
    product: Product,
    pointCost: Int,
    canRedeem: Boolean,
    pointsBalance: Int,
    onRedeemClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "$pointCost pts",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.secondary
                )
                if (!canRedeem) {
                    Text(
                        text = "Need ${pointCost - pointsBalance} more pts",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f)
                    )
                }
            }
            PrimaryButton(
                onClick = onRedeemClick,
                modifier = Modifier.width(100.dp),
                enabled = canRedeem
            ) {
                Text("Redeem", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RedeemRewardsPreview() {
    CodeCupTheme {
        RedeemRewardsScreen(onBackClick = {})
    }
}
