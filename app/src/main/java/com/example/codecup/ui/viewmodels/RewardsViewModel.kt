package com.example.codecup.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codecup.data.ProfileRepository
import com.example.codecup.data.UserProfile
import kotlinx.coroutines.flow.*

data class RewardsUiState(
    val user: UserProfile = UserProfile(),
    val isLoading: Boolean = false
)

class RewardsViewModel(
    private val profileRepository: ProfileRepository
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

    fun resetLoyaltyCard() {
        profileRepository.resetStamps()
    }
}
