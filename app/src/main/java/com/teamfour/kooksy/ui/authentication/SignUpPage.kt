package com.teamfour.kooksy.ui.authentication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.teamfour.kooksy.R
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import com.teamfour.kooksy.MainActivity
import com.teamfour.kooksy.ui.home.HomeFragment

class SignUpPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up_page)

        val signUpButton = findViewById<Button>(R.id.signinbutton)
        val loginText = findViewById<EditText>(R.id.signuploginbutton)

        // Simulate sign up and navigate to Home Page (Add actual sign up logic)
        signUpButton.setOnClickListener {
            // Assuming signup success
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Navigate to Login Page
        loginText.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }

       /* ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signuppage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
    }
}