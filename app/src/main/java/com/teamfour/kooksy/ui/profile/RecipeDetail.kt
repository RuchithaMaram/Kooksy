package com.teamfour.kooksy.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.teamfour.kooksy.databinding.FragmentRecipeDetailBinding

class RecipeDetail : Fragment() {
    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!
    private val args: RecipeDetailArgs by navArgs() // Retrieve the Recipe object from navArgs

    companion object {
        private const val TAG = "RecipeDetail"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Back button functionality
        binding.backButton.setOnClickListener {
            Log.d(TAG, "Back button clicked") // Debug log
            findNavController().popBackStack() // Navigate back
        }

        // Edit button functionality
        binding.editRecipeButton.setOnClickListener {
            val recipe = args.recipe
            if (recipe != null) {
                Log.d(TAG, "Edit button clicked for recipe: ${recipe.recipe_name}") // Debug log
                // Navigate to CreateFragment with the recipe data
                val action = RecipeDetailDirections.actionRecipeDetailToCreateFragment(recipe)
                findNavController().navigate(action)
            } else {
                Log.e(TAG, "Error: Recipe data is null")
            }
        }

        // Get the Recipe object from arguments
        val recipe = args.recipe
        if (recipe != null) {
            Log.d(TAG, "Loading recipe details for: ${recipe.recipe_name}")
            bindRecipeDetails(recipe) // Bind the recipe details to the UI
        } else {
            Log.e(TAG, "Error: Recipe data is null")
        }
    }


    // Function to bind the Recipe object to the UI
    private fun bindRecipeDetails(recipe: Recipe) {
        binding.recipeName.text = recipe.recipe_name
        binding.recipeCalories.text = "${recipe.recipe_calories} kcal"
        binding.recipeCookTime.text = "${recipe.recipe_cookTime} min"
        binding.recipeIngredients.text = recipe.recipe_ingredients.joinToString("\n") {
            "${it["ingredient_name"]}: ${it["ingredient_quantity"]}"
        }
        binding.recipeSteps.text = recipe.recipe_instructions.joinToString("\n")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
