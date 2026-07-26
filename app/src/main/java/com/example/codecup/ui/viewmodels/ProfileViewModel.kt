package com.example.codecup.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codecup.data.AppTheme
import com.example.codecup.data.OrderRepository
import com.example.codecup.data.ProfileRepository
import com.example.codecup.data.RewardsRepository
import com.example.codecup.data.UserProfile
import com.example.codecup.data.UserPreferencesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ProfileUiState(
    val user: UserProfile = UserProfile(),
    val ordersCount: Int = 0,
    val points: Int = 0,
    val isEditMode: Boolean = false,
    val themeMode: AppTheme = AppTheme.SYSTEM
)

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    rewardsRepository: RewardsRepository,
    orderRepository: OrderRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        profileRepository.profile.onEach { user ->
            _uiState.update { it.copy(user = user) }
        }.launchIn(viewModelScope)

        rewardsRepository.points.onEach { points ->
            _uiState.update { it.copy(points = points) }
        }.launchIn(viewModelScope)

        orderRepository.orderCount.onEach { count ->
            _uiState.update { it.copy(ordersCount = count) }
        }.launchIn(viewModelScope)

        userPreferencesRepository.themeMode.onEach { theme ->
            _uiState.update { it.copy(themeMode = theme) }
        }.launchIn(viewModelScope)
    }

    fun setThemeMode(theme: AppTheme) {
        viewModelScope.launch {
            userPreferencesRepository.setThemeMode(theme)
        }
    }

    fun toggleEditMode() {
        _uiState.update { it.copy(isEditMode = !it.isEditMode) }
    }

    fun updateProfile(name: String, email: String, phone: String) {
        viewModelScope.launch {
            profileRepository.updateProfile(name, email, phone)
            _uiState.update { it.copy(isEditMode = false) }
        }
    }
}
