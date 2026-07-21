package com.example.codecup.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.codecup.ui.theme.*

enum class NavDestination(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    Home("home", "Home", Icons.Filled.Home, Icons.Outlined.Home),
    Orders("orders", "Orders", Icons.Filled.Receipt, Icons.Outlined.Receipt),
    Rewards("rewards", "Rewards", Icons.Filled.Star, Icons.Outlined.Star),
    Profile("profile", "Profile", Icons.Filled.Person, Icons.Outlined.Person)
}

@Composable
fun BottomNavBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = CoffeeSurface,
        tonalElevation = 8.dp
    ) {
        NavDestination.values().forEach { destination ->
            val isSelected = currentRoute == destination.route
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(destination.route) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) destination.selectedIcon else destination.unselectedIcon,
                        contentDescription = destination.label
                    )
                },
                label = { Text(destination.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = CoffeeOnSecondaryContainer,
                    selectedTextColor = CoffeeOnSecondaryContainer,
                    indicatorColor = CoffeeSecondaryContainer,
                    unselectedIconColor = CoffeeOnSurfaceVariant,
                    unselectedTextColor = CoffeeOnSurfaceVariant
                )
            )
        }
    }
}
