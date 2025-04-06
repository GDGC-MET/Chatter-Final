package com.example.chatapp.uiScreens.ChatPage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatapp.models.ChatMessage

@Composable
fun ChatScreen(chatId: String) {
    val viewModel: ChatViewModel = viewModel()
    LaunchedEffect(Unit) {
        viewModel.initChat(chatId)
    }

    val messages by viewModel.messages.collectAsState()
    val inputText by viewModel.inputText.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            reverseLayout = true
        ) {
            items(messages.reversed()) { message ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (message.isFromMe) Arrangement.End else Arrangement.Start
                ) {
                    Text(
                        text = message.message,
                        modifier = Modifier
                            .padding(4.dp)
                            .background(
                                if (message.isFromMe) Color(0xFFD0F0C0) else Color.LightGray,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp),
                        color = Color.Black
                    )
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = inputText,
                onValueChange = { viewModel.setInputText(it) },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type a message") }
            )
            Button(onClick = { viewModel.sendMessage() }, modifier = Modifier.padding(start = 8.dp)) {
                Text("Send")
            }
        }
    }
}
