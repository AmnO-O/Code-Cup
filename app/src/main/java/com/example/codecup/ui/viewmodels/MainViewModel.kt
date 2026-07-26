package com.example.codecup.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codecup.data.AppTheme
import com.example.codecup.data.ProfileRepository
import com.example.codecup.data.UserProfile
import com.example.codecup.data.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    val themeMode: StateFlow<AppTheme> = userPreferencesRepository.themeMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppTheme.SYSTEM
        )

    val userProfile: StateFlow<UserProfile> = profileRepository.profile
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserProfile()
        )

    fun setThemeMode(theme: AppTheme) {
        viewModelScope.launch {
            userPreferencesRepository.setThemeMode(theme)
        }
    }
}
