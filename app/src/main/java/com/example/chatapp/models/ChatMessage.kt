package com.example.chatapp.models

data class ChatMessage(
    val id: String = "",
    val message: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isFromMe: Boolean = false
)