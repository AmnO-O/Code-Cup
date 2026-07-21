package com.example.codecup.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.codecup.ui.theme.CoffeeBackground
import com.example.codecup.ui.theme.CoffeeOnSurfaceVariant
import com.example.codecup.ui.theme.CoffeePrimary
import com.example.codecup.ui.theme.CoffeeSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader(
    title: String,
    onBackClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    centerTitle: Boolean = true
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = CoffeePrimary
                )
            )
        },
        navigationIcon = {
            if (onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = CoffeePrimary
                    )
                }
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = CoffeeBackground
        )
    )
}

@Composable
fun CartIconWithBadge(
    count: Int,
    onClick: () -> Unit
) {
    Box(modifier = Modifier.padding(end = 8.dp)) {
        IconButton(onClick = onClick) {
            Icon(
                Icons.Default.ShoppingCart,
                contentDescription = "Cart",
                tint = CoffeeOnSurfaceVariant
            )
        }
        if (count > 0) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .background(CoffeeSecondary, CircleShape)
                    .align(Alignment.TopEnd)
                    .offset(x = (-4).dp, y = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White
                )
            }
        }
    }
}
