package com.example.codecup.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.codecup.models.ChatMessage
import com.example.codecup.models.MessageSender
import com.example.codecup.models.Product
import com.example.codecup.ui.viewmodels.BaristaViewModel
import com.example.codecup.ui.viewmodels.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaristaScreen(
    onBackClick: () -> Unit,
    onProductClick: (Int) -> Unit,
    viewModel: BaristaViewModel = viewModel(factory = ViewModelFactory(context = LocalContext.current))
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    // Scroll to bottom when new messages arrive
    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "Ask the Barista", 
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color(0xFF31170B)
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, 
                            contentDescription = "Back",
                            tint = Color(0xFF31170B)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFDF9F4)
                )
            )
        },
        bottomBar = {
            ChatInputArea(
                text = uiState.inputText,
                onTextChanged = { viewModel.onInputTextChanged(it) },
                onSendClick = { viewModel.sendMessage() },
                onSuggestionClick = { viewModel.sendMessage(it) }
            )
        },
        containerColor = Color(0xFFFDF9F4)
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                DateDivider()
            }

            items(uiState.messages) { message ->
                MessageBubble(
                    message = message,
                    onProductClick = onProductClick
                )
            }

            if (uiState.isTyping) {
                item {
                    TypingIndicator()
                }
            }
        }
    }
}

@Composable
fun DateDivider() {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Surface(
            color = Color(0xFFF1EDE8),
            shape = CircleShape
        ) {
            Text(
                text = "Today, 9:41 AM",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF50443F)
            )
        }
    }
}

@Composable
fun MessageBubble(
    message: ChatMessage,
    onProductClick: (Int) -> Unit
) {
    val isBarista = message.sender == MessageSender.BARISTA
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isBarista) Arrangement.Start else Arrangement.End,
        verticalAlignment = Alignment.Top
    ) {
        if (isBarista) {
            BaristaAvatar()
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            modifier = Modifier.weight(1f, fill = false),
            horizontalAlignment = if (isBarista) Alignment.Start else Alignment.End
        ) {
            Surface(
                color = if (isBarista) Color.White else Color(0xFFC1502E),
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (isBarista) 0.dp else 16.dp,
                    bottomEnd = if (isBarista) 16.dp else 0.dp
                ),
                border = if (isBarista) BorderStroke(1.dp, Color(0xFFE4D6C9)) else null,
                tonalElevation = if (isBarista) 1.dp else 0.dp
            ) {
                Text(
                    text = message.text,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    color = if (isBarista) Color(0xFF2B211B) else Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            message.recommendedProduct?.let { product ->
                Spacer(modifier = Modifier.height(8.dp))
                RecommendationCard(product = product, onClick = { onProductClick(product.id) })
            }
        }
    }
}

@Composable
fun BaristaAvatar() {
    Surface(
        modifier = Modifier.size(32.dp),
        shape = CircleShape,
        color = Color(0xFFF3E4D7),
        border = BorderStroke(1.dp, Color(0xFFE4D6C9))
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                Icons.Default.LocalCafe,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = Color(0xFF31170B)
            )
        }
    }
}

@Composable
fun RecommendationCard(
    product: Product,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(260.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE4D6C9))
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color(0xFF31170B),
                    maxLines = 1
                )
                Text(
                    text = "Smooth, bold", // Placeholder description
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF50443F)
                )
                Text(
                    text = "$${"%.2f".format(product.price)}",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF31170B)
                )
            }
            Surface(
                modifier = Modifier.size(28.dp),
                shape = CircleShape,
                color = Color(0xFFF3E4D7)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add",
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFF31170B)
                )
            }
        }
    }
}

@Composable
fun ChatInputArea(
    text: String,
    onTextChanged: (String) -> Unit,
    onSendClick: () -> Unit,
    onSuggestionClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            val suggestions = listOf("Recommend a drink", "Low caffeine", "Dairy-free options")
            items(suggestions) { suggestion ->
                SuggestionChip(text = suggestion, onClick = { onSuggestionClick(suggestion) })
            }
        }

        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = { /* Add attachment */ },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(Icons.Default.AddCircle, contentDescription = null, tint = Color(0xFF50443F))
            }
            
            TextField(
                value = text,
                onValueChange = onTextChanged,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp),
                placeholder = { Text("Type a message...", color = Color(0xFF50443F)) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFFDF9F4),
                    unfocusedContainerColor = Color(0xFFFDF9F4),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(24.dp),
                maxLines = 4
            )

            IconButton(
                onClick = onSendClick,
                modifier = Modifier.size(40.dp),
                colors = IconButtonDefaults.iconButtonColors(contentColor = Color(0xFFC1502E))
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
            }
        }
    }
}

@Composable
fun SuggestionChip(text: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        shape = CircleShape,
        border = BorderStroke(1.dp, Color(0xFFE4D6C9)),
        color = Color.Transparent
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            color = Color(0xFF50443F)
        )
    }
}

@Composable
fun TypingIndicator() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = 40.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            "Barista is typing...",
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF50443F),
            modifier = Modifier.padding(4.dp)
        )
    }
}
