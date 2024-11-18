package com.teamfour.kooksy.ui.create

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreateViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance() // Firestore instance
    private val auth = FirebaseAuth.getInstance() // FirebaseAuth instance
    private val TAG = "CreateViewModel" // TAG for logging

    // Function to submit recipe to Firestore
    fun submitRecipe(
        recipeName: String,
        recipecal: Int,
        cookTime: Int,
        difficulty: String,
        ingredients: List<Map<String, String>>,
        steps: List<String>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val userId = auth.currentUser?.uid // Get the current user's ID
        if (userId == null) {
            onFailure(Exception("User not authenticated")) // Handle unauthenticated user
            return
        }

        if (recipeName.isNotEmpty() && ingredients.isNotEmpty() && steps.isNotEmpty()) {
            val recipeData = hashMapOf(
                "recipe_name" to recipeName,
                "recipe_imageURL" to "https://example.com/recipe_image.jpg", // Placeholder image URL
                "recipe_calories" to recipecal,
                "recipe_cookTime" to cookTime,
                "recipe_difficultyLevel" to difficulty,
                "recipe_ingredients" to ingredients,
                "recipe_instructions" to steps,
                "is_favourite" to false,
                "recipe_rating" to 0.0, // Rating to be added by other users later
                "createdBy" to userId, // Include userId for filtering
                "createdOn" to com.google.firebase.Timestamp.now() // Auto-generated timestamp
            )

            db.collection("RECIPE").add(recipeData)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "Recipe submitted with ID: ${documentReference.id}")
                    onSuccess()
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error submitting recipe", e)
                    onFailure(e)
                }
        } else {
            Log.w(TAG, "Submission failed: Missing required fields")
            onFailure(Exception("Please fill all required fields"))
        }
    }
}
