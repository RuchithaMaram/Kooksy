package com.teamfour.kooksy.ui.home

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.teamfour.kooksy.R
import com.teamfour.kooksy.databinding.ActivityRecipeFragmentBinding
import androidx.core.content.ContextCompat

class RecipeFragment : AppCompatActivity() {

    private lateinit var binding: ActivityRecipeFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recipe_fragment)
//        setEmojiDrawable()
    }

    private fun setEmojiDrawable(){
        var topDrawable: Drawable? = null
        var difficultyLevel: String = "hard"
        when(difficultyLevel.lowercase()){
            "easy" -> topDrawable = ContextCompat.getDrawable(this, R.drawable.hearteyes_emoji)
            "medium" -> topDrawable = ContextCompat.getDrawable(this,R.drawable.happy_emoji)
            "hard" -> topDrawable = ContextCompat.getDrawable(this,R.drawable.grinningface_emoji)
        }
        binding.recipeDifficultyLevel.setCompoundDrawablesWithIntrinsicBounds(null,topDrawable,null,null)
        Log.i("draw","drawed")


    }
}