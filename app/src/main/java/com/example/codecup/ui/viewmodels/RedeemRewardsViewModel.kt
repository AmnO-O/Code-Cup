package com.example.codecup.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.codecup.data.OrderRepository
import com.example.codecup.data.ProfileRepository
import com.example.codecup.models.CartItem
import com.example.codecup.models.Order
import com.example.codecup.models.OrderStatus
import com.example.codecup.models.Product
import com.example.codecup.workers.OrderStatusWorker
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class RedeemRewardsUiState(
    val pointsBalance: Int = 0,
    val isRedeeming: Boolean = false,
    val redeemSuccess: Boolean = false,
    val showConfirmDialog: Boolean = false,
    val selectedProduct: Product? = null,
    val showCelebration: Boolean = false
)

class RedeemRewardsViewModel(
    private val profileRepository: ProfileRepository,
    private val orderRepository: OrderRepository,
    private val context: Context? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(RedeemRewardsUiState())
    val uiState: StateFlow<RedeemRewardsUiState> = _uiState.asStateFlow()

    init {
        profileRepository.profile.onEach { user ->
            _uiState.update { it.copy(pointsBalance = user.points) }
        }.launchIn(viewModelScope)
    }

    fun initiateRedeem(product: Product) {
        _uiState.update { it.copy(showConfirmDialog = true, selectedProduct = product) }
    }

    fun dismissDialog() {
        _uiState.update { it.copy(showConfirmDialog = false, selectedProduct = null) }
    }

    fun confirmRedeem(takeNow: Boolean) {
        val product = _uiState.value.selectedProduct ?: return
        val cost = (product.price * 25).toInt()
        
        if (_uiState.value.pointsBalance >= cost) {
            viewModelScope.launch {
                _uiState.update { it.copy(isRedeeming = true, showConfirmDialog = false) }
                
                if (takeNow) {
                    // Deduct points only if taking now
                    profileRepository.redeemPoints(cost, "Redeemed ${product.name}")
                    
                    // Place free order
                    val orderId = "RE-${UUID.randomUUID().toString().take(6).uppercase()}"
                    val cartItem = CartItem(
                        product = product,
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
                } else {
                    // User canceled or decided not to spend points yet
                    // The user said "không nên trừ điểm" when save later/cancel
                    // So we just reset state
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
    }
    
    fun dismissCelebration() {
        _uiState.update { it.copy(showCelebration = false) }
    }
    
    fun resetSuccess() {
        _uiState.update { it.copy(redeemSuccess = false) }
    }
}
