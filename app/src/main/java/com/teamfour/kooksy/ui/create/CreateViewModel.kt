package com.teamfour.kooksy.ui.create

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.io.IOException

class CreateViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance() // Firestore instance
    private val auth = FirebaseAuth.getInstance() // FirebaseAuth instance
    private val TAG = "CreateViewModel" // TAG for logging

    private val _submitRecipeResult = MutableLiveData<Result<Boolean>>()
    val submitRecipeResult: LiveData<Result<Boolean>> get() = _submitRecipeResult

    private fun encodeImageToBase64(file: File): String? {
        return try {
            val bytes = file.readBytes()
            Base64.encodeToString(bytes, Base64.DEFAULT)
        } catch (e: IOException) {
            Log.e(TAG, "Error encoding image to Base64", e)
            null
        }
    }

    // Function to submit recipe to Firestore
    fun submitRecipe(
        recipeName: String,
        recipecal: Int,
        cookTime: Int,
        difficulty: String,
        ingredients: List<Map<String, String>>,
        steps: List<String>,
        imageURL: String?
    ) {
        val recipeData = hashMapOf(
            "recipe_name" to recipeName,
            "calories" to recipecal,
            "cook_time" to cookTime,
            "difficulty" to difficulty,
            "ingredients" to ingredients,
            "steps" to steps,
            "image_url" to imageURL  // Add imageURL here
        )

        // Add the recipe data to Firestore
        db.collection("recipes")
            .add(recipeData)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Recipe added with ID: ${documentReference.id}")
                // Post success to LiveData
                _submitRecipeResult.postValue(Result.success(true))
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding recipe", e)
                // Post failure to LiveData
                _submitRecipeResult.postValue(Result.failure(e))
            }
    }
}
