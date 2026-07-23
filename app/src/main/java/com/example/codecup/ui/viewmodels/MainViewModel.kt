package com.example.codecup.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codecup.data.AppTheme
import com.example.codecup.data.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val themeMode: StateFlow<AppTheme> = userPreferencesRepository.themeMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppTheme.SYSTEM
        )
}
