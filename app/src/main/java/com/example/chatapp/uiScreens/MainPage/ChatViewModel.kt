package com.example.chatapp.uiScreens.MainPage

import com.example.chatapp.models.Chat



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance().reference.child("chats")

    private val _chats = MutableStateFlow<List<Chat>>(emptyList())
    val chats: StateFlow<List<Chat>> = _chats.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadChats()
    }

    private fun loadChats() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _chats.value = snapshot.children.mapNotNull {
                    it.getValue(Chat::class.java)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _error.value = error.message
            }
        })
    }

    fun createChat(name: String, code: String) {
        viewModelScope.launch {
            try {
                val chatId = database.push().key ?: return@launch
                val chat = Chat(id = chatId, name = name, accessCode = code)

                database.child(chatId).setValue(chat)
                    .addOnFailureListener {
                        _error.value = "Failed to create chat: ${it.message}"
                    }
            } catch (e: Exception) {
                _error.value = "Failed to create chat: ${e.message}"
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
