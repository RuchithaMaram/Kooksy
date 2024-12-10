package com.teamfour.kooksy.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.teamfour.kooksy.utils.Utils

class MyRecipeViewModel : ViewModel() {

    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> = _recipes

    private val db = FirebaseFirestore.getInstance()
    //private val auth = FirebaseAuth.getInstance()

    companion object {
        private const val TAG = "MyRecipeViewModel"
    }

    init {
        fetchRecipesFromFirebase()
    }

    fun fetchRecipesFromFirebase() {
        db.collection("RECIPE")
            .get()
            .addOnSuccessListener { result ->
                val recipeList = result.mapNotNull { document ->
                    Utils.parseResponseToRecipe(document)
                }.distinctBy { it.documentId } // Ensure no duplicates

                Log.d(TAG, "Fetched ${recipeList.size} unique recipes from Firebase.")
                recipeList.forEach { recipe ->
                    Log.d(TAG, "Recipe fetched: ${recipe.recipe_name} (ID: ${recipe.documentId})")
                }

                _recipes.value = recipeList
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error fetching recipes from Firebase: ${exception.message}")
                exception.printStackTrace()
            }
    }
}
