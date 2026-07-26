package com.example.codecup.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.codecup.data.OrderRepository
import com.example.codecup.data.ProductRepository
import com.example.codecup.data.ProfileRepository
import com.example.codecup.data.UserProfile
import com.example.codecup.models.*
import com.example.codecup.workers.OrderStatusWorker
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

enum class RewardChoice {
    POINTS, FREE_DRINK
}

data class RewardsUiState(
    val user: UserProfile = UserProfile(),
    val isLoading: Boolean = false,
    val showRewardChoiceDialog: Boolean = false
)

class RewardsViewModel(
    private val profileRepository: ProfileRepository,
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
    private val context: Context? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(RewardsUiState())
    val uiState: StateFlow<RewardsUiState> = _uiState.asStateFlow()

    init {
        observeProfile()
    }

    private fun observeProfile() {
        profileRepository.profile.onEach { user ->
            _uiState.update { it.copy(user = user) }
        }.launchIn(viewModelScope)
    }

    fun onStampsCompleted() {
        if (_uiState.value.user.stamps >= 8) {
            _uiState.update { it.copy(showRewardChoiceDialog = true) }
        }
    }

    fun dismissDialog() {
        _uiState.update { it.copy(showRewardChoiceDialog = false) }
    }

    fun claimReward(choice: RewardChoice) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, showRewardChoiceDialog = false) }
            
            profileRepository.clearStamps()
            
            when (choice) {
                RewardChoice.POINTS -> {
                    profileRepository.addPoints(500, "Loyalty Reward")
                }
                RewardChoice.FREE_DRINK -> {
                    // Get the cheapest drink
                    productRepository.getProducts().firstOrNull()?.let { products ->
                        val cheapestProduct = products.minByOrNull { it.price }
                        if (cheapestProduct != null) {
                            val orderId = "RW-${UUID.randomUUID().toString().take(6).uppercase()}"
                            val cartItem = CartItem(
                                product = cheapestProduct,
                                quantity = 1,
                                size = "Regular",
                                shots = "Standard",
                                iceLevel = "Normal",
                                totalPrice = 0.0
                            )
                            val order = Order(
                                id = orderId,
                                date = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date()),
                                items = listOf(cartItem),
                                totalPrice = 0.0,
                                status = OrderStatus.Received
                            )
                            orderRepository.placeOrder(order)

                            // Trigger simulation
                            context?.let { ctx ->
                                val workRequest = OneTimeWorkRequestBuilder<OrderStatusWorker>()
                                    .setInputData(workDataOf("order_id" to orderId))
                                    .build()
                                WorkManager.getInstance(ctx).enqueue(workRequest)
                            }
                        }
                    }
                }
            }
            
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun resetLoyaltyCard() {
        profileRepository.resetStamps()
    }
}
