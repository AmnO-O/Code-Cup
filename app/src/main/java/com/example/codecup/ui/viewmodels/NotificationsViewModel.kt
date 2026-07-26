package com.example.codecup.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codecup.data.NotificationsRepository
import com.example.codecup.data.database.NotificationEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NotificationsUiState(
    val notifications: List<NotificationEntity> = emptyList(),
    val isLoading: Boolean = true
)

class NotificationsViewModel(
    private val notificationsRepository: NotificationsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    init {
        notificationsRepository.notifications.onEach { list ->
            _uiState.update { it.copy(notifications = list, isLoading = false) }
        }.launchIn(viewModelScope)
    }

    /** Called once the list is visible — clears the drawer's unread badge. */
    fun markAllRead() {
        viewModelScope.launch {
            notificationsRepository.markAllRead()
        }
    }
}
