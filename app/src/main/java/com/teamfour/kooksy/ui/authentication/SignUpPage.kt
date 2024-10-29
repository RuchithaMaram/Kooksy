package com.teamfour.kooksy.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.teamfour.kooksy.MainActivity
import com.teamfour.kooksy.R

class SignUpPage : AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up_page)

        val usernameField = findViewById<EditText>(R.id.signupusername)
        val emailField = findViewById<EditText>(R.id.signupemail)
        val passwordField = findViewById<EditText>(R.id.signuppassword)
        val confirmPasswordField = findViewById<EditText>(R.id.signupconfirmpassword)
        val signUpButton = findViewById<Button>(R.id.signinbutton)
        val loginButton = findViewById<TextView>(R.id.signuploginbutton)

        signUpButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()
            val confirmPassword = confirmPasswordField.text.toString().trim()
            val username = usernameField.text.toString().trim()

            // Username: 3-20 characters, alphanumeric or underscores
            if (username.length < 3 || username.length > 20 || !username.matches("^[a-zA-Z0-9_]+$".toRegex())) {
                Toast.makeText(this, "Invalid username", Toast.LENGTH_SHORT).show()
            }
            // Email: valid format ending in .com
            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() || !email.endsWith(".com")) {
                Toast.makeText(this, "Enter a valid email", Toast.LENGTH_SHORT).show()
            }
            // Password: at least 8 characters, includes uppercase, lowercase, digit, and special character
            else if (!password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}$".toRegex())) {
                Toast.makeText(this, "Weak password", Toast.LENGTH_SHORT).show()
            }
            // Passwords match check
            else if (password != confirmPassword) {
                Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show()
            }
            // If inputs are valid, proceed with sign-up
            else {
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = firebaseAuth.currentUser?.uid ?: return@addOnCompleteListener
                        val userRecord = mapOf(
                            "user_id" to userId,
                            "user_name" to username,
                            "email" to email,
                            "profilePhotoURL" to "",
                            "joinDate" to System.currentTimeMillis()
                        )

                        // Save user data in Firestore
                        val userRef = database.collection("users").document(userId)
                        userRef.set(userRecord).addOnSuccessListener {
                            // Add initial 'favorites' document
                            userRef.collection("favorites").add(
                                mapOf(
                                    "recipe_id" to 0, // Placeholder
                                    "savedOn" to System.currentTimeMillis()
                                )
                            )

                            // Add initial 'submittedRecipes' document
                            userRef.collection("submittedRecipes").add(
                                mapOf(
                                    "recipe_id" to 0, // Placeholder
                                    "submittedOn" to System.currentTimeMillis()
                                )
                            )

                            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                        }.addOnFailureListener { exception ->
                            Toast.makeText(this, "Save failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Sign-up failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        loginButton.setOnClickListener {
            startActivity(Intent(this, LoginPage::class.java))
        }

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signup_page)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
