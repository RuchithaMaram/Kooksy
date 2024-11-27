package com.teamfour.kooksy.ui.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamfour.kooksy.MyApp.Companion.db
import com.teamfour.kooksy.ui.profile.User
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    fun checkIfEmailExists(email: String, onResult: (User?) -> Unit) {
        var user: User? = null
        viewModelScope.launch {
            db.collection("users").whereEqualTo("email", email)
                .get().addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        for (document in querySnapshot) {
                            user = User(
                                email = document.getString("email") ?: "",
                                joinDate = document.getLong("joinDate") ?: 0,
                                profilePhotoURL = document.getString("profilePhotoURL") ?: "",
                                user_id = document.getString("user_id") ?: "",
                                user_name = document.getString("user_name") ?: ""
                            )
                        }
                        onResult(user)
                    } else {
                        // Email does not exist
                        onResult(null)
                    }
                }.addOnFailureListener { e ->
                    println("Error checking email existence $e")
                // Handle failure case gracefully
                }
        }
    }
}