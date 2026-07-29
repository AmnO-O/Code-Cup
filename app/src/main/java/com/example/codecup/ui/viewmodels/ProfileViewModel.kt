package com.example.codecup.ui.viewmodels

import android.net.Uri
import android.util.Patterns
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
    val themeMode: AppTheme = AppTheme.SYSTEM,
    val emailError: String? = null
)

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    rewardsRepository: RewardsRepository,
    private val orderRepository: OrderRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    /** One-shot snackbar messages ("Profile updated"). */
    private val _events = MutableSharedFlow<String>()
    val events: SharedFlow<String> = _events.asSharedFlow()

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
        _uiState.update { it.copy(isEditMode = !it.isEditMode, emailError = null) }
    }

    /** Validates on save (ui_design §3.8): invalid email keeps edit mode with an inline error. */
    fun updateProfile(name: String, email: String, phone: String) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            _uiState.update { it.copy(emailError = "Please enter a valid email address") }
            return
        }
        viewModelScope.launch {
            profileRepository.updateProfile(name.trim(), email.trim(), phone.trim())
            _uiState.update { it.copy(isEditMode = false, emailError = null) }
            _events.emit("Profile updated")
        }
    }

    fun updateAvatar(pickedUri: Uri) {
        viewModelScope.launch {
            val saved = profileRepository.updateAvatarFromUri(pickedUri)
            _events.emit(if (saved) "Profile photo updated" else "Couldn't load that photo")
        }
    }

    fun resetOrderHistory() {
        viewModelScope.launch {
            orderRepository.clearOrderHistory()
            _events.emit("Order history cleared")
        }
    }
}
