package com.example.codecup.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.codecup.ui.components.*
import com.example.codecup.ui.theme.*

@Composable
fun ProfileScreen(
    onNavigate: (String) -> Unit
) {
    var isEditMode by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("Alex Johnson") }
    var email by remember { mutableStateOf("alex.johnson@example.com") }
    var phone by remember { mutableStateOf("+1 (555) 123-4567") }

    Scaffold(
        topBar = {
            AppHeader(
                title = "Profile",
                actions = {
                    IconButton(onClick = { isEditMode = !isEditMode }) {
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
            
            // Avatar
            Surface(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                color = CoffeePrimaryContainer
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxSize(),
                    tint = Color.White
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = name,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = CoffeeOnSurface
            )
            Text(
                text = email,
                style = MaterialTheme.typography.bodyMedium,
                color = CoffeeOnSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Stats Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ProfileStat(label = "Orders", value = "24")
                ProfileStat(label = "Points", value = "1,240")
                ProfileStat(label = "Member since", value = "Jan 2024")
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Form
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ProfileField(label = "Full Name", value = name, isEditMode = isEditMode, onValueChange = { name = it })
                ProfileField(label = "Email Address", value = email, isEditMode = isEditMode, onValueChange = { email = it })
                ProfileField(label = "Phone Number", value = phone, isEditMode = isEditMode, onValueChange = { phone = it })
            }
            
            if (isEditMode) {
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SecondaryButton(
                        onClick = { isEditMode = false },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }
                    PrimaryButton(
                        onClick = { isEditMode = false },
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
fun ProfileStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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

@Composable
fun ProfileField(
    label: String,
    value: String,
    isEditMode: Boolean,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = CoffeeOnSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        if (isEditMode) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CoffeePrimary,
                    unfocusedBorderColor = CoffeeOutline
                )
            )
        } else {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = CoffeeOnSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CoffeeSurface, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            )
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
