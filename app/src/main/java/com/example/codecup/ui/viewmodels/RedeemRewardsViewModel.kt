package com.example.codecup.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codecup.data.ProfileRepository
import com.example.codecup.models.Product
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class RedeemRewardsUiState(
    val pointsBalance: Int = 0,
    val isRedeeming: Boolean = false,
    val redeemSuccess: Boolean = false
)

class RedeemRewardsViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RedeemRewardsUiState())
    val uiState: StateFlow<RedeemRewardsUiState> = _uiState.asStateFlow()

    init {
        profileRepository.profile.onEach { user ->
            _uiState.update { it.copy(pointsBalance = user.points) }
        }.launchIn(viewModelScope)
    }

    fun redeemProduct(product: Product) {
        val cost = (product.price * 25).toInt()
        if (_uiState.value.pointsBalance >= cost) {
            viewModelScope.launch {
                _uiState.update { it.copy(isRedeeming = true) }
                profileRepository.redeemPoints(cost, "Redeemed ${product.name}")
                _uiState.update { it.copy(isRedeeming = false, redeemSuccess = true) }
            }
        }
    }
    
    fun resetSuccess() {
        _uiState.update { it.copy(redeemSuccess = false) }
    }
}
