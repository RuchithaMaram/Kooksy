package com.teamfour.kooksy.ui.create

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.teamfour.kooksy.ui.profile.UserDetails

class CreateViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance() // Firestore instance
    private val auth = FirebaseAuth.getInstance() // FirebaseAuth instance
    private val TAG = "CreateViewModel" // TAG for logging

    // Declare a LiveData to hold the image URL
    private val _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String> get() = _imageUrl

    // Set the image URL in the ViewModel
    fun setImageUrl(url: String) {
        _imageUrl.value = url
    }

    // Function to submit recipe to Firestore
    fun submitRecipe(
        recipeName: String,
        recipecal: Int,
        cookTime: Int,
        difficulty: String,
        ingredients: List<Map<String, String>>,
        steps: List<String>,
        imageUrl: String?,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val userId = UserDetails.user?.user_id
        if (userId == null) {
            onFailure(Exception("User not authenticated")) // Handle unauthenticated user
            return
        }

        if (recipeName.isNotEmpty() && ingredients.isNotEmpty() && steps.isNotEmpty()) {
            val recipeData = hashMapOf(
                "recipe_name" to recipeName,
                "recipe_imageURL" to imageUrl, // Placeholder image URL
                "recipe_calories" to recipecal,
                "recipe_cookTime" to cookTime,
                "recipe_difficultyLevel" to difficulty,
                "recipe_ingredients" to ingredients,
                "recipe_instructions" to steps,
                "is_favourite" to false,
                "averageRating" to 0.0, // Rating to be added by other users later
                "ratingCount" to 0.0,
                "totalRating" to 0.0,
                "createdBy" to userId, // Include userId for filtering
                "createdOn" to com.google.firebase.Timestamp.now(), // Auto-generated timestamp
                "ratedBy" to emptyList<String>()

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
