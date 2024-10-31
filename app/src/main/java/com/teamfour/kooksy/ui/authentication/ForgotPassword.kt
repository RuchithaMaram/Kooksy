package com.teamfour.kooksy.ui.authentication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.teamfour.kooksy.R
import android.content.Intent
import android.widget.ImageButton
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.teamfour.kooksy.ui.authentication.LoginPage

class ForgotPassword : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        auth = FirebaseAuth.getInstance() // Initialize FirebaseAuth

        val newPasswordField = findViewById<EditText>(R.id.forgotpassword)
        val confirmPasswordField = findViewById<EditText>(R.id.forgotconfirmpassword)
        val submitButton = findViewById<Button>(R.id.forgotsubmit)
        val backButton = findViewById<ImageButton>(R.id.forgotPageBackButton)

        backButton.setOnClickListener{
               val intent = Intent(this,LoginPage::class.java)
            startActivity(intent)
        }

        submitButton.setOnClickListener {
            val newPassword = newPasswordField.text.toString().trim()
            val confirmPassword = confirmPasswordField.text.toString().trim()

            // Validate passwords
            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in both password fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (newPassword != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!newPassword.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}$".toRegex())) {
                Toast.makeText(this, "Password must be min 8 char having a capital letter, numerical and a special characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Update password in Firebase Auth
            val user = auth.currentUser
            user?.let {
                it.updatePassword(newPassword).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
                        // Navigate back to LoginPage
                        val intent = Intent(this, LoginPage::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Password update failed. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }
            } ?: run {
                Toast.makeText(this, "No user logged in. Please log in first.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
