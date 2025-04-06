package com.example.chatapp.uiScreens.MainPage

import androidx.compose.foundation.layout.*


import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chatapp.uiScreens.MainPage.Components.JoinChatDialog
import com.example.chatapp.uiScreens.MainPage.Components.NewChatDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatMainScreen(
    navController: NavController,
) {
    var showNewChatDialog by remember { mutableStateOf(false) }
    var showJoinChatDialog by remember { mutableStateOf(false) }
    // Empty chat list that can be populated from your data source
    val chatList = remember { mutableStateListOf<String>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ChatApp") },
                actions = {
                    IconButton(onClick = { showJoinChatDialog = true }) {
                        Icon(Icons.Default.Search, contentDescription = "Join existing chat")
                    }
                }
            )
        }   ,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showNewChatDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add new chat")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (chatList.isEmpty()) {
                // Empty state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No chats yet. Create or join a chat to get started.")
                }
            } else {
                // Chat list
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    items(chatList) { chatName ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            shape = RoundedCornerShape(8.dp),
                            onClick = {  val chatId = chatName.lowercase().replace(" ", "_")
                                navController.navigate("chat/$chatId")}
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = chatName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
            // New Chat Dialog
            if (showNewChatDialog) {
                NewChatDialog(
                    onDismiss = { showNewChatDialog = false },
                    onCreateChat = { name, code ->
                        chatList.add(name)
                        showNewChatDialog = false
                    }
                )
            }
            // Join Chat Dialog
            if (showJoinChatDialog) {
                JoinChatDialog(
                    onDismiss = { showJoinChatDialog = false },
                    onJoinChat = { name, code ->
                        chatList.add(name)
                        showJoinChatDialog = false
                    }
                )
            }
        }
    }
}