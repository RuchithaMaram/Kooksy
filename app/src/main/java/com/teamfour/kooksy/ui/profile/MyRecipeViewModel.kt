package com.teamfour.kooksy.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

// [MyRecipeViewModel] fetches recipe data from Firebase Firestore and provides it as LiveData
// for UI components like RecyclerView. It retrieves recipes from the "RECIPE" collection.


class MyRecipeViewModel : ViewModel() {

    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> = _recipes

    private val db = FirebaseFirestore.getInstance()

    init {
        fetchRecipesFromFirebase()
    }

// Utilized ChatGPT for implementation of Firestore data fetching.

    private fun fetchRecipesFromFirebase() {
        db.collection("RECIPE")
            .get()
            .addOnSuccessListener { result ->
                val recipeList = mutableListOf<Recipe>()
                for (document in result) {
                    val recipe = Recipe(
                        recipe_name = document.getString("recipe_name") ?: "",
                        recipe_calories = document.getLong("recipe_calories")?.toInt() ?: 0,
                        recipe_cookTime = document.getLong("recipe_cookTime")?.toInt() ?: 0,
                        recipe_difficultyLevel = document.getString("recipe_difficultyLevel") ?: "",
                        recipe_imageURL = document.getString("recipe_imageURL") ?: "",
                        recipe_ingredients = document.get("recipe_ingredients") as? List<Map<String, String>>
                            ?: emptyList(),
                        recipe_instructions = document.get("recipe_instructions") as? List<String>
                            ?: emptyList(),
                        createdOn = document.getTimestamp("createdOn"),  // Keep as Timestamp
                        recipe_rating = 0.0,
                        //is_favourite = false
                    )
                    recipeList.add(recipe)
                }
                _recipes.value = recipeList
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace() // Log error
            }
    }
}