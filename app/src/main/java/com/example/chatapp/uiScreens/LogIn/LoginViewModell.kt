package com.example.chatapp.uiScreens.LogIn

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class LogInViewModel @Inject constructor(): ViewModel() {


    val Auth = FirebaseAuth.getInstance()

    fun LogIN(navController: NavController, email:String, password:String,){

        Auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
            if(it.isSuccessful){

                navController.navigate("home")

            }
        }

    }



}