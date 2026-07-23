package com.example.codecup.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codecup.data.AppTheme
import com.example.codecup.data.ProfileRepository
import com.example.codecup.data.UserProfile
import com.example.codecup.data.UserPreferencesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ProfileUiState(
    val user: UserProfile = UserProfile(),
    val isEditMode: Boolean = false,
    val isLoading: Boolean = false,
    val themeMode: AppTheme = AppTheme.SYSTEM
)

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        observeProfile()
        observeTheme()
    }

    private fun observeProfile() {
        profileRepository.profile.onEach { user ->
            _uiState.update { it.copy(user = user) }
        }.launchIn(viewModelScope)
    }

    private fun observeTheme() {
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

    fun signOut() {
        // Implement sign out logic here
    }
}
