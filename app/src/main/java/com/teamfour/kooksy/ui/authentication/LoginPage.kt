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
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.teamfour.kooksy.MainActivity
import com.teamfour.kooksy.databinding.ActivityLoginPageBinding
import com.teamfour.kooksy.ui.home.HomeFragment

class LoginPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_page)

        val loginButton = findViewById<Button>(R.id.loginbutton)
        val forgotPasswordText = findViewById<TextView>(R.id.loginforgotpage)
        val signUpText = findViewById<TextView>(R.id.loginsignup)


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


        loginButton.setOnClickListener {
            // Assuming login success
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }



       /* ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signuppage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
    }
}
