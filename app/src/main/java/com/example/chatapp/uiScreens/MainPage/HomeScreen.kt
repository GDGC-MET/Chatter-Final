package com.example.chatapp.uiScreens.MainPage

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatapp.models.Chat
import com.example.chatapp.ui.theme.*
import com.example.chatapp.uiScreens.MainPage.Components.JoinChatDialog
import com.example.chatapp.uiScreens.MainPage.Components.NewChatDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun ChatMainScreen(
    viewModel: HomeScreenViewModel = viewModel(),
    onChatSelected: (chatId: String, chatName: String) -> Unit = { _, _ -> }
) {
    val chats by viewModel.chats.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    var showNewChatDialog by remember { mutableStateOf(false) }
    var showJoinChatDialog by remember { mutableStateOf(false) }

    // Handle error messages with Snackbar
    LaunchedEffect(error) {
        error?.let {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "Error: $it",
                    duration = SnackbarDuration.Short
                )
            }
            viewModel.clearError()
        }
    }

    val surfaceColor = MaterialTheme.colorScheme.surface
    val gradientColors = listOf(
        surfaceColor.copy(alpha = 0.95f),
        surfaceColor.copy(alpha = 0.85f)
    )

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        "ChatApp",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                actions = {
                    IconButton(
                        onClick = { showJoinChatDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = "Join existing chat",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showNewChatDialog = true },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                expanded = !listState.isScrollInProgress,
                icon = {
                    Icon(
                        Icons.Rounded.Add,
                        contentDescription = "Create chat"
                    )
                },
                text = { Text("New Chat") }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Brush.verticalGradient(gradientColors))
        ) {
            AnimatedContent(
                targetState = Triple(isLoading, chats.isEmpty(), chats),
                transitionSpec = {
                    fadeIn(animationSpec = tween(400)) with
                            fadeOut(animationSpec = tween(200))
                },
                label = "content"
            ) { (loading, isEmpty, chatsList) ->
                when {
                    loading -> LoadingScreen()
                    isEmpty -> EmptyStateScreen()
                    else -> ChatsList(
                        chats = chatsList,
                        listState = listState,
                        onChatSelected = onChatSelected
                    )
                }
            }

            // New Chat Dialog with animation
            AnimatedVisibility(
                visible = showNewChatDialog,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                NewChatDialog(
                    onDismiss = { showNewChatDialog = false },
                    onCreateChat = { name, code ->
                        viewModel.createChat(name, code)
                        showNewChatDialog = false
                        scope.launch {
                            snackbarHostState.showSnackbar("Creating chat room: $name")
                        }
                    }
                )
            }

            // Join Chat Dialog with animation
            AnimatedVisibility(
                visible = showJoinChatDialog,
                enter = fadeIn() + slideInVertically { it / 2 },
                exit = fadeOut() + slideOutVertically { it / 2 }
            ) {
                JoinChatDialog(
                    onDismiss = { showJoinChatDialog = false },
                    onJoinChat = { name, code ->
                        viewModel.joinChat(name, code)
                        showJoinChatDialog = false
                        scope.launch {
                            snackbarHostState.showSnackbar("Joining chat room: $name")
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val infiniteTransition = rememberInfiniteTransition(label = "loading")
        val scale by infiniteTransition.animateFloat(
            initialValue = 0.8f,
            targetValue = 1.2f,
            animationSpec = infiniteRepeatable(
                animation = tween(800),
                repeatMode = RepeatMode.Reverse
            ),
            label = "scale"
        )

        CircularProgressIndicator(
            modifier = Modifier.scale(scale),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@Composable
private fun EmptyStateScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.Chat,
            contentDescription = null,
            modifier = Modifier.size(72.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "No chats yet",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Create or join a chat to get started",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun ChatsList(
    chats: List<Chat>,
    listState: LazyListState,
    onChatSelected: (chatId: String, chatName: String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        state = listState,
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = chats,
            key = { it.id }
        ) { chat ->
            val interactionSource = remember { MutableInteractionSource() }
            var isPressed by remember { mutableStateOf(false) }

            val elevation by animateFloatAsState(
                targetValue = if (isPressed) 1f else 4f,
                label = "elevation"
            )

            LaunchedEffect(interactionSource) {
                interactionSource.interactions.collect { interaction ->
                    when (interaction) {
                        is PressInteraction.Press -> isPressed = true
                        is PressInteraction.Release, is PressInteraction.Cancel -> isPressed = false
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItemPlacement()
                    .shadow(elevation.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                onClick = { onChatSelected(chat.id, chat.name) },
                interactionSource = interactionSource
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Avatar or icon for the chat
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.secondary
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = chat.name.take(1).uppercase(),
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = chat.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Tap to open chat",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }

                    Icon(
                        imageVector = Icons.Rounded.ChevronRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}