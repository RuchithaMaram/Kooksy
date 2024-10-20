package com.teamfour.kooksy.splashscreens

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.teamfour.kooksy.R
import com.teamfour.kooksy.ui.authentication.LandingPage

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.splash_screen_1)
        navigateToSplashScreens()
    }

    private fun navigateToSplashScreens(){
        //Navigates to splash screen 2
        findViewById<ImageButton>(R.id.rightArrowImageButton1)?.setOnClickListener{
            setContentView(R.layout.splash_screen_2)
            navigateToSplashScreens()
        }

        //Navigates to splash screen 3
        findViewById<ImageButton>(R.id.rightArrowImageButton2)?.setOnClickListener{
            setContentView(R.layout.splash_screen_3)
            navigateToSplashScreens()
        }

        //Navigates to Landing Page
        findViewById<ImageButton>(R.id.rightArrowImageButton3)?.setOnClickListener{
            val explicitIntent = Intent(this,LandingPage::class.java)
            startActivity(explicitIntent)
            //Closes the current activity
            finish()
        }
    }
}