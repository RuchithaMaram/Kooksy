package com.teamfour.kooksy

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

open class BaseActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        progressBar = findViewById(R.id.progressBar)
    }

    fun showProgressBar() {
        progressBar.visibility = ProgressBar.VISIBLE
    }

    fun hideProgressBar(delayMillis: Long) {
        Handler(Looper.getMainLooper()).postDelayed({
            progressBar.visibility = View.GONE
        }, delayMillis)
    }
}