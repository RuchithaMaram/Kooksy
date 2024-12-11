package com.teamfour.kooksy.ui.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.teamfour.kooksy.utils.Utils

class MyRecipeViewModel : ViewModel() {

    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> = _recipes

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    companion object {
        private const val TAG = "MyRecipeViewModel"
    }

    init {
        fetchRecipesFromFirebase()
    }

    fun fetchRecipesFromFirebase() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Log.e(TAG, "User is not logged in. Cannot fetch recipes.")
            _recipes.value = emptyList()
            return
        }

        db.collection("RECIPE")
            .whereEqualTo("createdBy", userId) // Filter recipes by the logged-in user's ID
            .get()
            .addOnSuccessListener { result ->
                val recipeList = result.mapNotNull { document ->
                    Utils.parseResponseToRecipe(document)
                }.distinctBy { it.documentId } // Ensure no duplicates

                Log.d(TAG, "Fetched ${recipeList.size} recipes for user $userId.")
                recipeList.forEach { recipe ->
                    Log.d(TAG, "User Recipe: ${recipe.recipe_name} (ID: ${recipe.documentId})")
                }

                _recipes.value = recipeList
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error fetching recipes from Firebase for user $userId: ${exception.message}")
                exception.printStackTrace()
            }
    }

    fun deleteRecipe(recipeId: String) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Log.e(TAG, "User is not logged in. Cannot delete recipe.")
            return
        }

        db.collection("RECIPE")
            .document(recipeId)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "Recipe with ID $recipeId successfully deleted for user $userId.")
                fetchRecipesFromFirebase() // Refresh the recipe list
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error deleting recipe with ID $recipeId: ${exception.message}")
                exception.printStackTrace()
            }
    }
}
