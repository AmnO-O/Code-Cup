package com.example.codecup.models

import java.util.Date

enum class MessageSender {
    USER, BARISTA
}

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String,
    val sender: MessageSender,
    val timestamp: Date = Date(),
    val recommendedProduct: Product? = null
)
