package com.teamfour.kooksy.ui.home

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.teamfour.kooksy.R
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.teamfour.kooksy.databinding.ActivityRecipeFragmentBinding
import com.teamfour.kooksy.ui.home.data.RecipeData
import com.teamfour.kooksy.ui.profile.Recipe
import org.w3c.dom.Text

class RecipeFragment : AppCompatActivity() {

//    private lateinit var binding: ActivityRecipeFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recipe_fragment)

        //Retrive the data from parcelable object
       var recipe = intent.getParcelableExtra<Recipe>("recipe_details")
            ?: throw IllegalArgumentException("Recipe not found")
        Log.i("RecipeDetails","$recipe")

        displayRecipeDetails(recipe)

        // Back navigation
        findViewById<ImageView>(R.id.recipePageBackNav).setOnClickListener {
            finish() // This will navigate back to the previous screen
        }
    }

    private fun displayRecipeDetails(recipe: Recipe){
        findViewById<TextView>(R.id.recipePageHeader).text = "${recipe.recipe_name}"
        findViewById<TextView>(R.id.recipeCookingTime).text = "${recipe.recipe_cookTime} MINS"
        findViewById<TextView>(R.id.recipeDifficultyLevel).text = "${recipe.recipe_difficultyLevel}"
        findViewById<TextView>(R.id.recipeCalories).text = "${recipe.recipe_calories} CAL"

        setEmojiDrawable(recipe.recipe_difficultyLevel)
    }

    private fun setEmojiDrawable(difficultyLevel: String){
        var topDrawable: Drawable? = null
        when(difficultyLevel.lowercase()){
            "easy" -> topDrawable = ContextCompat.getDrawable(this, R.drawable.hearteyes_emoji)
            "medium" -> topDrawable = ContextCompat.getDrawable(this,R.drawable.happy_emoji)
            "hard" -> topDrawable = ContextCompat.getDrawable(this,R.drawable.grinningface_emoji)
        }
        findViewById<TextView>(R.id.recipeDifficultyLevel).setCompoundDrawablesWithIntrinsicBounds(null,topDrawable,null,null)
        Log.i("draw","drawed")
    }
}