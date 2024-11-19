package com.teamfour.kooksy.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyRecipeViewModel : ViewModel() {

    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> = _recipes

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    init {
        fetchRecipesFromFirebase()
    }

    private fun fetchRecipesFromFirebase() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            _recipes.value = emptyList()
            return
        }

        db.collection("RECIPE")
            .whereEqualTo("createdBy", userId)
            .get()
            .addOnSuccessListener { result ->
                val recipeList = mutableListOf<Recipe>()
                for (document in result) {
                    val recipe = Recipe(
                        documentId = document.id, // Capture the document ID
                        recipe_name = document.getString("recipe_name") ?: "",
                        recipe_calories = document.getLong("recipe_calories")?.toInt() ?: 0,
                        recipe_cookTime = document.getLong("recipe_cookTime")?.toInt() ?: 0,
                        recipe_difficultyLevel = document.getString("recipe_difficultyLevel") ?: "",
                        recipe_imageURL = document.getString("recipe_imageURL") ?: "",
                        recipe_ingredients = document.get("recipe_ingredients") as? List<Map<String, String>> ?: emptyList(),
                        recipe_instructions = document.get("recipe_instructions") as? List<String> ?: emptyList(),
                        createdOn = document.getTimestamp("createdOn"),
                        recipe_rating = document.getDouble("recipe_rating") ?: 0.0,
                        is_favourite = document.getBoolean("is_favourite") ?: false
                    )
                    recipeList.add(recipe)
                }
                _recipes.value = recipeList
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
            }
    }
}
