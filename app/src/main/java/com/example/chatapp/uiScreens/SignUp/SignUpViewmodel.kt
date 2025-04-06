package com.example.chatapp.uiScreens.SignUp


import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.chatapp.models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.*
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    ) : ViewModel() {
    private val auth = getInstance()

    fun signUp(email: String, password: String, username: String,navController: NavController) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val userId = FirebaseAuth.getInstance().currentUser!!.uid
                    val user = Users(
                        id = userId,
                        username = username,
                        email = email
                    )
                    FirebaseDatabase.getInstance().getReference("Users").child(userId)
                        .setValue(user)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(
                                    context,
                                    "User signin succesfull",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                navController.navigate("home")


                            } else {
                                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()

                            }
                        }
                }
            }
    }
}