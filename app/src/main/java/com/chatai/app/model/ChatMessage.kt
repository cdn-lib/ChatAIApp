package com.chatai.app.model

data class ChatMessage(
    val role: String,       // "user" or "assistant"
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isLoading: Boolean = false
)
