package com.teamfour.kooksy.ui.authentication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.teamfour.kooksy.R
import android.widget.Button
import android.content.Intent

class ForgotPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgot_password)

        val submitButton = findViewById<Button>(R.id.forgotsubmit)

        submitButton.setOnClickListener {
            // Submit new password logic here
            // After submitting, navigate back to login or home
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }

    /*    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signuppage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        } */
    }
}