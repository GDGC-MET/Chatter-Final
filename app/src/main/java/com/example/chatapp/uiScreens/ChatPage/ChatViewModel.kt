package com.example.chatapp.uiScreens.ChatPage

import androidx.lifecycle.ViewModel
import com.example.chatapp.models.ChatMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChatViewModel : ViewModel() {

    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    private val currentUserName = FirebaseAuth.getInstance().currentUser?.displayName ?: "Me"

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    private var chatId: String = ""
    private var database: DatabaseReference? = null

    fun initChat(chatId: String) {
        if (this.chatId.isNotEmpty()) return // prevent reinitialization
        this.chatId = chatId
        this.database = FirebaseDatabase.getInstance().reference
            .child("chats")
            .child(chatId)
            .child("messages")

        listenForMessages()
    }

    fun setInputText(text: String) {
        _inputText.value = text
    }

    fun sendMessage() {
        val text = inputText.value.trim()
        if (text.isEmpty()) return

        val messageId = database?.push()?.key ?: return
        val message = ChatMessage(
            id = messageId,
            message = text,
            senderId = currentUserId,
            senderName = currentUserName,
            timestamp = System.currentTimeMillis(),
            isFromMe = true
        )

        database?.child(messageId)?.setValue(message)
        _inputText.value = ""
    }

    private fun listenForMessages() {
        database?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messageList = mutableListOf<ChatMessage>()
                for (child in snapshot.children) {
                    val message = child.getValue(ChatMessage::class.java)
                    message?.let {
                        messageList.add(it.copy(isFromMe = it.senderId == currentUserId))
                    }
                }
                _messages.value = messageList
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
