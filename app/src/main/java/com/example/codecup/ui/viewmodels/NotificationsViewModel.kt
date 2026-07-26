package com.example.codecup.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codecup.data.NotificationsRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NotificationUiModel(
    val id: Long,
    val title: String,
    val body: String,
    val dateText: String,
    val isRead: Boolean
)

data class NotificationsUiState(
    val notifications: List<NotificationUiModel> = emptyList(),
    val isLoading: Boolean = true
)

class NotificationsViewModel(
    private val notificationsRepository: NotificationsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    init {
        notificationsRepository.notifications.onEach { list ->
            val formatter = SimpleDateFormat("dd MMMM, HH:mm", Locale.getDefault())
            val models = list.map { entity ->
                NotificationUiModel(
                    id = entity.id,
                    title = entity.title,
                    body = entity.body,
                    dateText = formatter.format(Date(entity.dateMillis)),
                    isRead = entity.isRead
                )
            }
            _uiState.update { it.copy(notifications = models, isLoading = false) }
        }.launchIn(viewModelScope)
    }

    /** Called once the list is visible — clears the drawer's unread badge. */
    fun markAllRead() {
        viewModelScope.launch {
            notificationsRepository.markAllRead()
        }
    }
}
