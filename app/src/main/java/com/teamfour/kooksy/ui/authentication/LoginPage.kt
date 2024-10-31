package com.teamfour.kooksy.ui.authentication

import android.os.Bundle
import android.content.Intent
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.teamfour.kooksy.MainActivity
import com.teamfour.kooksy.R
import com.teamfour.kooksy.ui.home.HomeFragment

class LoginPage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_page)

        auth = FirebaseAuth.getInstance()
        emailInput = findViewById(R.id.loginemail)  // Assuming you have an EditText for email
        passwordInput = findViewById(R.id.loginpassword)  // Assuming you have an EditText for password
        val loginButton = findViewById<Button>(R.id.loginbutton)
        val forgotPasswordText = findViewById<TextView>(R.id.loginforgotpage)
        val signUpText = findViewById<TextView>(R.id.loginsignup)
        val skipButton = findViewById<Button>(R.id.skipbutton)  // Add a button for skipping login

        // Navigate to Forgot Password Page
        forgotPasswordText.setOnClickListener {
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
        }

        // Navigate to Sign Up Page
        signUpText.setOnClickListener {
            val intent = Intent(this, SignUpPage::class.java)
            startActivity(intent)
        }

        // Skip to HomeFragment
        skipButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("fragment", HomeFragment::class.java.name) // Optional: pass a flag if needed
            startActivity(intent)
        }

        // Handle login button click
        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            // Validate input
            if (validateEmail(email) && validatePassword(password)) {
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Please enter valid email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateEmail(email: String): Boolean {
        // Use regex to validate email format ending in .com
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.endsWith(".com")
    }


    private fun validatePassword(password: String): Boolean {
        // Password validation: at least 8 characters, includes uppercase, lowercase, digit, and special character
        return password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}$".toRegex())
    }


    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login success, navigate to MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Close the LoginPage
                } else {
                    // If sign in fails, display a message to the user
                    Toast.makeText(this, "Authentication failed. Please check your credentials.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
