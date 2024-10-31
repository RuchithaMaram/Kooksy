package com.teamfour.kooksy.ui.authentication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.teamfour.kooksy.R
import android.content.Intent
import android.widget.Button
import com.teamfour.kooksy.MainActivity

class LandingPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_landing_page)

        val loginButton = findViewById<Button>(R.id.LandingLogin)
        val signupButton = findViewById<Button>(R.id.landingsignup)

        //Navigate to login page
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }

        //Navigate to signup page
        signupButton.setOnClickListener {
            val intent = Intent(this, SignUpPage::class.java)
            startActivity(intent)
        }
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signuppage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets*/
    }
}