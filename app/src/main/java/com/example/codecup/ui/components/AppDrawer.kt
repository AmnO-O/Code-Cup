package com.example.codecup.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.codecup.data.AppTheme
import com.example.codecup.data.UserProfile
import com.example.codecup.data.UserPreferencesRepository
import com.example.codecup.ui.viewmodels.MainViewModel
import com.example.codecup.ui.viewmodels.ViewModelFactory

sealed class DrawerDestination(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val hasBadge: Boolean = false,
    val badgeCount: Int = 0
) {
    object Home : DrawerDestination("home", "Home", Icons.Default.Home)
    object Favorites : DrawerDestination("favorites", "Favorites", Icons.Default.FavoriteBorder)
    object Barista : DrawerDestination("barista", "Ask the Barista", Icons.Default.ChatBubbleOutline)
    object Notifications : DrawerDestination("notifications", "Notifications", Icons.Default.NotificationsNone, hasBadge = true, badgeCount = 2)
    object Orders : DrawerDestination("orders", "My Orders", Icons.Default.Receipt)
    object Rewards : DrawerDestination("rewards", "Rewards", Icons.Default.StarBorder)
    object Profile : DrawerDestination("profile", "Profile", Icons.Default.PersonOutline)
    object About : DrawerDestination("about", "Help & About", Icons.AutoMirrored.Filled.HelpOutline)
}

@Composable
fun AppDrawer(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onCloseDrawer: () -> Unit,
    viewModel: MainViewModel = viewModel(
        factory = ViewModelFactory(
            userPreferencesRepository = UserPreferencesRepository.getInstance(LocalContext.current)
        )
    )
) {
    val themeMode by viewModel.themeMode.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val isDarkMode = themeMode == AppTheme.DARK

    ModalDrawerSheet(
        drawerContainerColor = Color(0xFFFFFFFF),
        drawerTonalElevation = 0.dp,
        drawerShape = RoundedCornerShape(topEnd = 0.dp, bottomEnd = 0.dp),
        modifier = Modifier.width(320.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxHeight()
        ) {
            // Header
            DrawerHeader(user = userProfile)

            Spacer(modifier = Modifier.height(16.dp))

            // Menu Items
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                DrawerItem(
                    destination = DrawerDestination.Favorites,
                    isSelected = currentRoute == DrawerDestination.Favorites.route,
                    onNavigate = onNavigate,
                    onCloseDrawer = onCloseDrawer
                )
                DrawerItem(
                    destination = DrawerDestination.Barista,
                    isSelected = currentRoute == DrawerDestination.Barista.route,
                    onNavigate = onNavigate,
                    onCloseDrawer = onCloseDrawer
                )
                DrawerItem(
                    destination = DrawerDestination.Notifications,
                    isSelected = currentRoute == DrawerDestination.Notifications.route,
                    onNavigate = onNavigate,
                    onCloseDrawer = onCloseDrawer
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
                    color = Color(0xFFD4C3BC).copy(alpha = 0.5f)
                )

                // Dark Mode Toggle Item
                DarkModeToggleItem(
                    isDarkMode = isDarkMode,
                    onToggle = { checked ->
                        viewModel.setThemeMode(if (checked) AppTheme.DARK else AppTheme.LIGHT)
                    }
                )

                DrawerItem(
                    destination = DrawerDestination.About,
                    isSelected = currentRoute == DrawerDestination.About.route,
                    onNavigate = onNavigate,
                    onCloseDrawer = onCloseDrawer
                )
            }

            // Footer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = Color(0xFFE4D6C9).copy(alpha = 0.3f), shape = RoundedCornerShape(0.dp))
                    .padding(vertical = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "App Version 2.1.0",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF50443F).copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun DrawerHeader(user: UserProfile) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF7F3EE))
            .padding(start = 24.dp, top = 48.dp, bottom = 24.dp)
    ) {
        Box(modifier = Modifier.padding(bottom = 16.dp)) {
            // Profile Image
            Surface(
                modifier = Modifier
                    .size(64.dp)
                    .border(2.dp, Color(0xFFF3E4D7), CircleShape),
                shape = CircleShape
            ) {
                AsyncImage(
                    model = user.avatarUrl,
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            // Verified Badge
            Surface(
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 2.dp, y = 2.dp),
                shape = CircleShape,
                color = Color(0xFFA53C1B)
            ) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = null,
                    modifier = Modifier.padding(2.dp),
                    tint = Color.White
                )
            }
        }

        Text(
            text = user.name,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF31170B)
            )
        )

        Spacer(modifier = Modifier.height(4.dp))

        Surface(
            color = Color(0xFFF3E4D7),
            shape = CircleShape
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Stars,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFFBE927F)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${user.points} pts",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFBE927F)
                    )
                )
            }
        }
    }
}

@Composable
fun DrawerItem(
    destination: DrawerDestination,
    isSelected: Boolean,
    onNavigate: (String) -> Unit,
    onCloseDrawer: () -> Unit
) {
    NavigationDrawerItem(
        label = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = destination.label,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                )
                if (destination.hasBadge && destination.badgeCount > 0) {
                    Surface(
                        color = Color(0xFFE5E2DD),
                        shape = CircleShape
                    ) {
                        Text(
                            text = destination.badgeCount.toString(),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFF50443F)
                        )
                    }
                }
            }
        },
        selected = isSelected,
        onClick = {
            onNavigate(destination.route)
            onCloseDrawer()
        },
        icon = {
            Box {
                Icon(
                    imageVector = destination.icon,
                    contentDescription = null,
                    tint = if (isSelected) Color(0xFF31170B) else Color(0xFF50443F)
                )
                if (destination.route == "notifications") {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Color(0xFFA53C1B), CircleShape)
                            .border(1.5.dp, Color.White, CircleShape)
                            .align(Alignment.TopEnd)
                            .offset(x = 2.dp, y = (-2).dp)
                    )
                }
            }
        },
        shape = RoundedCornerShape(8.dp),
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = Color(0xFFF1EDE8),
            unselectedContainerColor = Color.Transparent,
            selectedIconColor = Color(0xFF31170B),
            unselectedIconColor = Color(0xFF50443F),
            selectedTextColor = Color(0xFF31170B),
            unselectedTextColor = Color(0xFF2B211B)
        ),
        modifier = Modifier.padding(vertical = 2.dp)
    )
}

@Composable
fun DarkModeToggleItem(
    isDarkMode: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onToggle(!isDarkMode) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.DarkMode,
            contentDescription = null,
            tint = Color(0xFF50443F)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Dark Mode",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Medium,
                color = Color(0xFF2B211B)
            )
        )
        Switch(
            checked = isDarkMode,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF31170B),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFE5E2DD),
                uncheckedBorderColor = Color(0xFFD4C3BC)
            )
        )
    }
}
