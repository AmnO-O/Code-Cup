package com.example.codecup.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.codecup.data.OrderRepository
import com.example.codecup.data.ProductRepository
import com.example.codecup.data.RewardsRepository
import com.example.codecup.domain.PriceCalculator
import com.example.codecup.models.CartItem
import com.example.codecup.models.Order
import com.example.codecup.models.OrderStatus
import com.example.codecup.models.PointsHistoryItem
import com.example.codecup.workers.OrderStatusWorker
import java.util.UUID
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class RewardChoice {
    POINTS, FREE_DRINK
}

data class RewardsUiState(
    val stamps: Int = 0,
    val points: Int = 0,
    val pointsHistory: List<PointsHistoryItem> = emptyList(),
    val showRewardChoiceDialog: Boolean = false,
    val showCelebration: Boolean = false
)

class RewardsViewModel(
    private val rewardsRepository: RewardsRepository,
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
    private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(RewardsUiState())
    val uiState: StateFlow<RewardsUiState> = _uiState.asStateFlow()

    init {
        rewardsRepository.stamps.onEach { stamps ->
            _uiState.update { it.copy(stamps = stamps) }
        }.launchIn(viewModelScope)

        rewardsRepository.points.onEach { points ->
            _uiState.update { it.copy(points = points) }
        }.launchIn(viewModelScope)

        rewardsRepository.pointsHistory.onEach { history ->
            _uiState.update { it.copy(pointsHistory = history) }
        }.launchIn(viewModelScope)
    }

    fun onStampsCompleted() {
        if (_uiState.value.stamps >= RewardsRepository.STAMPS_PER_CARD) {
            _uiState.update { it.copy(showRewardChoiceDialog = true) }
        }
    }

    fun dismissDialog() {
        _uiState.update { it.copy(showRewardChoiceDialog = false) }
    }

    /** Rubric: Loyalty Card Reset — explicit user action once the card is full. */
    fun claimReward(choice: RewardChoice) {
        viewModelScope.launch {
            _uiState.update { it.copy(showRewardChoiceDialog = false) }
            rewardsRepository.clearStamps()

            when (choice) {
                RewardChoice.POINTS -> {
                    rewardsRepository.addPoints(RewardsRepository.FULL_CARD_BONUS_POINTS, "Loyalty Reward")
                }
                RewardChoice.FREE_DRINK -> {
                    val cheapest = productRepository.getProducts().first().minByOrNull { it.price }
                    if (cheapest != null) {
                        val orderId = "RW-${UUID.randomUUID().toString().take(6).uppercase()}"
                        val order = Order(
                            id = orderId,
                            dateMillis = System.currentTimeMillis(),
                            items = listOf(
                                CartItem(
                                    product = cheapest,
                                    quantity = 1,
                                    size = PriceCalculator.SIZE_MEDIUM,
                                    shots = PriceCalculator.SHOTS_DOUBLE,
                                    iceLevel = PriceCalculator.ICE_REGULAR,
                                    totalPrice = 0.0
                                )
                            ),
                            totalPrice = 0.0,
                            status = OrderStatus.Received
                        )
                        orderRepository.placeOrder(order)

                        val workRequest = OneTimeWorkRequestBuilder<OrderStatusWorker>()
                            .setInputData(workDataOf(OrderStatusWorker.KEY_ORDER_ID to orderId))
                            .build()
                        WorkManager.getInstance(context).enqueue(workRequest)
                    }
                }
            }

            _uiState.update { it.copy(showCelebration = true) }
        }
    }

    fun dismissCelebration() {
        _uiState.update { it.copy(showCelebration = false) }
    }
}
