package com.example.codecup.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.codecup.ui.components.*
import com.example.codecup.ui.theme.*
import com.example.codecup.ui.viewmodels.ProfileViewModel
import com.example.codecup.ui.viewmodels.ViewModelFactory

@Composable
fun ProfileScreen(
    onNavigate: (String) -> Unit,
    viewModel: ProfileViewModel = viewModel(factory = ViewModelFactory())
) {
    val uiState by viewModel.uiState.collectAsState()
    val user = uiState.user
    val isEditMode = uiState.isEditMode

    var editedName by remember(user.name) { mutableStateOf(user.name) }
    var editedEmail by remember(user.email) { mutableStateOf(user.email) }
    var editedPhone by remember(user.phone) { mutableStateOf(user.phone) }

    Scaffold(
        topBar = {
            AppHeader(
                title = "Profile",
                actions = {
                    IconButton(onClick = { 
                        if (isEditMode) {
                            viewModel.updateProfile(editedName, editedEmail, editedPhone)
                        } else {
                            viewModel.toggleEditMode()
                        }
                    }) {
                        Icon(
                            imageVector = if (isEditMode) Icons.Default.Check else Icons.Default.Edit,
                            contentDescription = if (isEditMode) "Save" else "Edit",
                            tint = CoffeePrimary
                        )
                    }
                }
            )
        },
        bottomBar = {
            if (!isEditMode) {
                BottomNavBar(currentRoute = NavDestination.Profile.route, onNavigate = onNavigate)
            }
        },
        containerColor = CoffeeBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            // Avatar with Edit Trigger
            Box(contentAlignment = Alignment.BottomEnd) {
                Surface(
                    modifier = Modifier
                        .size(100.dp)
                        .border(2.dp, CoffeeOutline, CircleShape),
                    shape = CircleShape,
                    color = CoffeePrimaryContainer
                ) {
                    AsyncImage(
                        model = user.avatarUrl,
                        contentDescription = "Avatar",
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                
                Surface(
                    modifier = Modifier
                        .size(32.dp)
                        .shadow(4.dp, CircleShape)
                        .clickable { /* Trigger photo picker */ },
                    shape = CircleShape,
                    color = CoffeeSecondary,
                    contentColor = Color.White
                ) {
                    Icon(
                        Icons.Default.PhotoCamera,
                        contentDescription = "Change Photo",
                        modifier = Modifier.padding(6.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = user.name,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = CoffeeOnSurface
            )
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodyMedium,
                color = CoffeeOnSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Stats Row (Bento Grid Style)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProfileStatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.AutoMirrored.Filled.ReceiptLong,
                    label = "Orders",
                    value = user.ordersCount.toString()
                )
                ProfileStatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Stars,
                    label = "Points",
                    value = user.points.toString()
                )
                ProfileStatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.CalendarMonth,
                    label = "Joined",
                    value = user.joinedYear
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Personal Information Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                color = CoffeeSurface,
                border = BorderStroke(1.dp, CoffeeOutline)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Personal Information",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = CoffeePrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = CoffeeOutlineVariant)
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    ProfileField(
                        label = "Full Name",
                        value = editedName,
                        icon = Icons.Default.Person,
                        isEditMode = isEditMode,
                        onValueChange = { editedName = it }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ProfileField(
                        label = "Email Address",
                        value = editedEmail,
                        icon = Icons.Default.Mail,
                        isEditMode = isEditMode,
                        onValueChange = { editedEmail = it }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ProfileField(
                        label = "Phone Number",
                        value = editedPhone,
                        icon = Icons.Default.Phone,
                        isEditMode = isEditMode,
                        onValueChange = { editedPhone = it }
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider(color = CoffeeOutlineVariant)
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Sign Out Button
                    PrimaryButton(
                        onClick = { viewModel.signOut() },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        containerColor = CoffeePrimaryContainer,
                        contentColor = Color.White
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Sign Out", fontWeight = FontWeight.Bold)
                    }
                }
            }
            
            if (isEditMode) {
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SecondaryButton(
                        onClick = { viewModel.toggleEditMode() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }
                    PrimaryButton(
                        onClick = { viewModel.updateProfile(editedName, editedEmail, editedPhone) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Save Changes")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ProfileStatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    value: String
) {
    Surface(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(12.dp),
        color = CoffeeSurface,
        border = BorderStroke(1.dp, CoffeeOutline),
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CoffeeSecondary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = CoffeePrimary
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = CoffeeOnSurfaceVariant
            )
        }
    }
}

@Composable
fun ProfileField(
    label: String,
    value: String,
    icon: ImageVector,
    isEditMode: Boolean,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = CoffeeOnSurfaceVariant
        )
        Spacer(modifier = Modifier.height(6.dp))
        if (isEditMode) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(icon, contentDescription = null, tint = CoffeeOnSurfaceVariant, modifier = Modifier.size(20.dp))
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CoffeePrimary,
                    unfocusedBorderColor = CoffeeOutline,
                    focusedContainerColor = CoffeeBackground.copy(alpha = 0.5f),
                    unfocusedContainerColor = CoffeeBackground.copy(alpha = 0.5f)
                ),
                singleLine = true
            )
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CoffeeBackground.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .border(1.dp, CoffeeOutline, RoundedCornerShape(12.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = CoffeeOnSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = CoffeeOnSurface
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    CodeCupTheme {
        ProfileScreen(onNavigate = {})
    }
}
