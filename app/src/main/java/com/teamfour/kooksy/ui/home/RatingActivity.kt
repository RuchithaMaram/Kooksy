package com.teamfour.kooksy.ui.home

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.teamfour.kooksy.R
import com.teamfour.kooksy.databinding.ActivityRatingBinding
import kotlin.random.Random

class RatingActivity : AppCompatActivity() {

    private lateinit var binding:ActivityRatingBinding
    private var selectedRating = 0F
    val random = Random.Default

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //Search for Rating Page and set it to screen
        binding = ActivityRatingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sadWithTearsEmoji = binding.sadWithTearsEmoji
        val sadEmoji = binding.sadEmoji
        val happyEmoji = binding.happyEmoji
        val grinningEmoji = binding.grinningFaceEmoji
        val heartEyesEmoji = binding.heartEyesEmoji

        sadWithTearsEmoji.setOnClickListener{setRating(0.0F,1.0F)}
        sadEmoji.setOnClickListener{setRating(1.0F,2.0F)}
        happyEmoji.setOnClickListener{setRating(2.0F,3.0F)}
        grinningEmoji.setOnClickListener{setRating(3.0F,4.0F)}
        heartEyesEmoji.setOnClickListener{setRating(4.0F,5.0F)}

        binding.submit.setOnClickListener{

        }
    }

    private fun setRating(minNumber:Float,maxNumber:Float){
        selectedRating = generateRandomFloat(minNumber,maxNumber)
    }

    private fun generateRandomFloat(min:Float,max:Float):Float{
        val randomNumber = min + (random.nextFloat() * (max-min))
        val roundedNumber= (Math.floor(randomNumber * 10.0) / 10.0).toFloat()
        Log.i("rating","$roundedNumber")
        return roundedNumber
    }
}