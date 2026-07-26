package com.example.codecup.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codecup.data.ProductRepository
import com.example.codecup.models.ChatMessage
import com.example.codecup.models.MessageSender
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BaristaUiState(
    val messages: List<ChatMessage> = listOf(
        ChatMessage(
            text = "Hello! I'm your Artisan Barista. How can I craft your perfect cup today?",
            sender = MessageSender.BARISTA
        )
    ),
    val inputText: String = "",
    val isTyping: Boolean = false
)

class BaristaViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BaristaUiState())
    val uiState: StateFlow<BaristaUiState> = _uiState.asStateFlow()

    fun onInputTextChanged(text: String) {
        _uiState.update { it.copy(inputText = text) }
    }

    fun sendMessage(text: String = _uiState.value.inputText) {
        if (text.isBlank()) return

        val userMessage = ChatMessage(text = text, sender = MessageSender.USER)
        _uiState.update { 
            it.copy(
                messages = it.messages + userMessage,
                inputText = ""
            )
        }

        generateResponse(text)
    }

    private fun generateResponse(userText: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isTyping = true) }
            delay(1500) // Simulate typing

            val lowerText = userText.lowercase()
            val (responseText, recommendationId) = when {
                lowerText.contains("strong") || lowerText.contains("caffeine") -> 
                    "If you need a real kick, I highly recommend our Espresso or Nitro Cold Brew. They're bold and packed with energy!" to 3 // Assuming 3 is a strong drink
                lowerText.contains("refreshing") || lowerText.contains("hot") -> 
                    "For a hot day, nothing beats our Iced Americano or Nitro Cold Brew. Very refreshing and smooth!" to 5 // Assuming 5 is Iced Americano
                lowerText.contains("dairy") || lowerText.contains("milk") || lowerText.contains("vegan") ->
                    "We have great oat and almond milk alternatives! Our Iced Latte with Oat Milk is a crowd favorite." to 2
                lowerText.contains("recommend") || lowerText.contains("suggest") ->
                    "I'd suggest our signature House Blend. It's perfectly balanced for any time of day." to 1
                else -> 
                    "That sounds interesting! Would you like to try something from our signature collection?" to -1
            }

            val recommendedProduct = if (recommendationId != -1) {
                productRepository.getProductById(recommendationId)
            } else null

            val baristaMessage = ChatMessage(
                text = responseText,
                sender = MessageSender.BARISTA,
                recommendedProduct = recommendedProduct
            )

            _uiState.update { 
                it.copy(
                    messages = it.messages + baristaMessage,
                    isTyping = false
                )
            }
        }
    }
}
