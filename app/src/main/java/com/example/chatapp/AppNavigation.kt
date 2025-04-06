package com.example.chatapp


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chatapp.uiScreens.ChatPage.ChatScreen
import com.example.chatapp.uiScreens.LogIn.LoginScreen
import com.example.chatapp.uiScreens.MainPage.ChatMainScreen
import com.example.chatapp.uiScreens.SignUp.SignUpScreen



@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "Login",
        modifier = modifier
    ) {
        // Home Screen
        composable("home") {
            ChatMainScreen(navController)
        }

        // Chat Screen
        composable(
            route = "chat/{chatId}",
            arguments = listOf(navArgument("chatId") { type = NavType.StringType })
        ) {
            val chatId = it.arguments?.getString("chatId") ?: ""
            ChatScreen(chatId)
        }

        composable("Login"){
            LoginScreen(navController)
        }

        composable("SignUp"){
            SignUpScreen(navController)
        }
    }
}