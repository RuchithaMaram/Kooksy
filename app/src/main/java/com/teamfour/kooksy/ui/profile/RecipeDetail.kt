package com.teamfour.kooksy.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.FirebaseFirestore
import com.teamfour.kooksy.databinding.FragmentRecipeDetailBinding

class RecipeDetail : Fragment() {
    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!
    private val args: RecipeDetailArgs by navArgs()
    private val firestore = FirebaseFirestore.getInstance()

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

        // Set up back button functionality
        binding.backButton.setOnClickListener {
            findNavController().popBackStack() // Navigate back
        }

        val recipeName = args.recipeName
        Log.d(TAG, "Loading recipe details for: $recipeName")
        loadRecipeDetails(recipeName)
    }

    private fun loadRecipeDetails(recipeName: String) {
        firestore.collection("RECIPE")
            .whereEqualTo("recipe_name", recipeName)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val recipe = document.toObject(Recipe::class.java)
                    if (recipe != null) {
                        Log.d(TAG, "Recipe loaded: ${recipe.recipe_name}")
                        // Bind the recipe details to the UI
                        binding.recipeName.text = recipe.recipe_name
                        binding.recipeCalories.text = "${recipe.recipe_calories} kcal"
                        binding.recipeCookTime.text = "${recipe.recipe_cookTime} min"
                        binding.recipeIngredients.text = recipe.recipe_ingredients.joinToString("\n") {
                            "${it["ingredient_name"]}: ${it["ingredient_quantity"]}"
                        }
                        binding.recipeSteps.text = recipe.recipe_instructions.joinToString("\n")
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error loading recipe details", e)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
