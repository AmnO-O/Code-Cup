package com.example.codecup.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codecup.data.CartRepository
import com.example.codecup.data.ProductRepository
import com.example.codecup.domain.PriceCalculator
import com.example.codecup.models.CartItem
import com.example.codecup.models.ChatMessage
import com.example.codecup.models.MessageSender
import com.example.codecup.models.Product
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
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
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BaristaUiState())
    val uiState: StateFlow<BaristaUiState> = _uiState.asStateFlow()

    /** One-shot snackbar events (e.g. "Added to cart"). */
    private val _events = MutableSharedFlow<String>()
    val events: SharedFlow<String> = _events.asSharedFlow()

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

    /** Adds a recommendation card's product to the cart with default customization. */
    fun addRecommendationToCart(product: Product) {
        viewModelScope.launch {
            cartRepository.addToCart(
                CartItem(
                    product = product,
                    quantity = 1,
                    size = if (product.category == "Cakes") PriceCalculator.SIZE_SLICE else PriceCalculator.SIZE_MEDIUM,
                    shots = PriceCalculator.SHOTS_DOUBLE,
                    iceLevel = PriceCalculator.ICE_REGULAR,
                    totalPrice = PriceCalculator.totalPrice(
                        product.price, 
                        if (product.category == "Cakes") PriceCalculator.SIZE_SLICE else PriceCalculator.SIZE_MEDIUM, 
                        PriceCalculator.SHOTS_DOUBLE, 
                        1,
                        product.category
                    )
                )
            )
            _events.emit("${product.name} added to cart")
        }
    }

    /**
     * Rule-based reply. Recommendations are resolved against the actual seeded menu
     * (looked up by name at reply time), so the card always refers to a real product.
     */
    private fun generateResponse(userText: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isTyping = true) }
            delay(TYPING_DELAY_MS)

            val menu = productRepository.getProducts().first()
            fun byName(name: String): Product? = menu.find { it.name == name }

            val lowerText = userText.lowercase()
            val (responseText, recommendation) = when {
                lowerText.contains("strong") || lowerText.contains("caffeine") && !lowerText.contains("low") ->
                    "If you need a real kick, our Classic Espresso is bold and packed with energy — the Nitro Cold Brew is a great pick too!" to byName("Classic Espresso")
                lowerText.contains("low") || lowerText.contains("decaf") ->
                    "For something gentler, try the Caramel Macchiato with a single shot — all the flavor, less of the buzz." to byName("Caramel Macchiato")
                lowerText.contains("refreshing") || lowerText.contains("iced") || lowerText.contains("cold") || lowerText.contains("hot day") ->
                    "For a hot day, nothing beats our Iced Americano — crisp, clean, and very refreshing!" to byName("Iced Americano")
                lowerText.contains("dairy") || lowerText.contains("milk") || lowerText.contains("vegan") ->
                    "We have great plant-based options! Our Oat Milk Latte is a crowd favorite — silky and completely dairy-free." to byName("Oat Milk Latte")
                lowerText.contains("sweet") || lowerText.contains("caramel") ->
                    "Sweet tooth? The Caramel Macchiato layers vanilla, caramel, and espresso — it's our most indulgent cup." to byName("Caramel Macchiato")
                lowerText.contains("hungry") || lowerText.contains("food") || lowerText.contains("snack") || lowerText.contains("pastry") ->
                    "A coffee is better with company — our Butter Croissant is baked fresh every morning!" to byName("Butter Croissant")
                lowerText.contains("recommend") || lowerText.contains("suggest") ->
                    "I'd suggest our signature Artisan Cappuccino. It's perfectly balanced for any time of day." to byName("Artisan Cappuccino")
                else ->
                    "That sounds interesting! Would you like to try something from our signature collection? The Artisan Cappuccino never disappoints." to byName("Artisan Cappuccino")
            }

            val baristaMessage = ChatMessage(
                text = responseText,
                sender = MessageSender.BARISTA,
                recommendedProduct = recommendation
            )

            _uiState.update {
                it.copy(
                    messages = it.messages + baristaMessage,
                    isTyping = false
                )
            }
        }
    }

    companion object {
        const val TYPING_DELAY_MS = 1_500L
    }
}
