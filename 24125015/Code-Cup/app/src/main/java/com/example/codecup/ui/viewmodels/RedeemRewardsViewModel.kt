package com.example.codecup.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.codecup.data.NotificationsRepository
import com.example.codecup.data.OrderRepository
import com.example.codecup.data.ProductRepository
import com.example.codecup.data.RewardsRepository
import com.example.codecup.domain.PriceCalculator
import com.example.codecup.models.CartItem
import com.example.codecup.models.Order
import com.example.codecup.models.OrderStatus
import com.example.codecup.models.Product
import com.example.codecup.workers.OrderStatusWorker
import java.util.UUID
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class RedeemRewardsUiState(
    val pointsBalance: Int = 0,
    val products: List<Product> = emptyList(),
    val isRedeeming: Boolean = false,
    val redeemSuccess: Boolean = false,
    val showConfirmDialog: Boolean = false,
    val selectedProduct: Product? = null,
    val showCelebration: Boolean = false
)

class RedeemRewardsViewModel(
    private val rewardsRepository: RewardsRepository,
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
    private val notificationsRepository: NotificationsRepository,
    private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(RedeemRewardsUiState())
    val uiState: StateFlow<RedeemRewardsUiState> = _uiState.asStateFlow()

    init {
        rewardsRepository.points.onEach { points ->
            _uiState.update { it.copy(pointsBalance = points) }
        }.launchIn(viewModelScope)

        productRepository.getProducts().onEach { products ->
            _uiState.update { it.copy(products = products) }
        }.launchIn(viewModelScope)
    }

    fun initiateRedeem(product: Product) {
        _uiState.update { it.copy(showConfirmDialog = true, selectedProduct = product) }
    }

    fun dismissDialog() {
        _uiState.update { it.copy(showConfirmDialog = false, selectedProduct = null) }
    }

    /**
     * Deducts points and places a free order when confirmed. Declining the dialog
     * leaves the balance untouched.
     */
    fun confirmRedeem(takeNow: Boolean) {
        val product = _uiState.value.selectedProduct ?: return
        val cost = RewardsRepository.redeemCostFor(product.price)

        if (_uiState.value.pointsBalance < cost) {
            dismissDialog()
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isRedeeming = true, showConfirmDialog = false) }

            if (takeNow) {
                rewardsRepository.redeemPoints(cost, "Redeemed ${product.name}")

                val orderId = "RE-${UUID.randomUUID().toString().take(6).uppercase()}"
                val order = Order(
                    id = orderId,
                    dateMillis = System.currentTimeMillis(),
                    items = listOf(
                        CartItem(
                            product = product,
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
                notificationsRepository.add(
                    title = "Reward redeemed",
                    body = "${product.name} redeemed for $cost pts — enjoy!"
                )

                val workRequest = OneTimeWorkRequestBuilder<OrderStatusWorker>()
                    .setInputData(workDataOf(OrderStatusWorker.KEY_ORDER_ID to orderId))
                    .build()
                WorkManager.getInstance(context).enqueue(workRequest)
            }

            _uiState.update {
                it.copy(
                    isRedeeming = false,
                    redeemSuccess = takeNow,
                    selectedProduct = null,
                    showCelebration = takeNow
                )
            }
        }
    }

    fun dismissCelebration() {
        _uiState.update { it.copy(showCelebration = false) }
    }

    fun resetSuccess() {
        _uiState.update { it.copy(redeemSuccess = false) }
    }
}
