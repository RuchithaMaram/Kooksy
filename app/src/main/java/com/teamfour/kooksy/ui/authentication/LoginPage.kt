package com.teamfour.kooksy.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.teamfour.kooksy.MainActivity
import com.teamfour.kooksy.R
import com.teamfour.kooksy.ui.profile.UserDetails

class LoginPage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        auth = FirebaseAuth.getInstance()

        val loginButton = findViewById<Button>(R.id.loginbutton)
        val emailField = findViewById<EditText>(R.id.loginemailtext)
        val passwordField = findViewById<EditText>(R.id.loginpassword)
        val forgotPasswordText = findViewById<TextView>(R.id.loginforgotpage)
        val signUpText = findViewById<TextView>(R.id.loginsignup)



        // Navigate to Forgot Password Page
        forgotPasswordText.setOnClickListener {
            startActivity(Intent(this, ForgotPassword::class.java))
        }

        // Navigate to Sign Up Page
        signUpText.setOnClickListener {
            startActivity(Intent(this, SignUpPage::class.java))
        }

        // Login Button Click Listener
        loginButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            // Firebase Authentication for login
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Successfully authenticated
                        loginViewModel.checkIfEmailExists(email) { user ->
                            if (user != null) {
                                UserDetails.user = user
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish() // Close the login activity
                            } else {
                                // User not found in firestore database
                                Toast.makeText(this, "User details not found. Please try again.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        // Authentication failed
                        Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }

        }
    }
}
