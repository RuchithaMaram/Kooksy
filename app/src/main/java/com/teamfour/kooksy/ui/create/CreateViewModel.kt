package com.teamfour.kooksy.ui.create

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class CreateViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance() // Firestore instance
    private val auth = FirebaseAuth.getInstance() // FirebaseAuth instance
    private val TAG = "CreateViewModel" // TAG for logging

    // Function to upload image to Firebase Storage
    fun uploadImageToStorage(uri: Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("images/${UUID.randomUUID()}.jpg")

        // Upload the image to Firebase Storage
        imageRef.putFile(uri)
            .addOnSuccessListener { taskSnapshot ->
                // Get the download URL after successful upload
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    val imageUrl = downloadUri.toString() // Get the image URL
                    onSuccess(imageUrl) // Pass the URL to onSuccess callback
                }
            }
            .addOnFailureListener { e ->
                onFailure(e) // Pass the exception to onFailure callback
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
        imageUri: Uri?, // Add imageUri parameter
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val userId = auth.currentUser?.uid // Get the current user's ID
        if (userId == null) {
            onFailure(Exception("User not authenticated"))
            return
        }

        if (recipeName.isNotEmpty() && ingredients.isNotEmpty() && steps.isNotEmpty()) {
            // If an image URI is provided, upload it to Firebase Storage and get the URL
            if (imageUri != null) {
                uploadImageToStorage(imageUri, { imageUrl ->
                    // After getting the image URL, proceed with submitting the recipe to Firestore
                    val recipeData = hashMapOf(
                        "recipe_name" to recipeName,
                        "recipe_imageURL" to (imageUrl ?: ""), // Use the dynamic image URL
                        "recipe_calories" to recipecal,
                        "recipe_cookTime" to cookTime,
                        "recipe_difficultyLevel" to difficulty,
                        "recipe_ingredients" to ingredients,
                        "recipe_instructions" to steps,
                        "is_favourite" to false,
                        "recipe_rating" to 0.0,
                        "createdBy" to userId,
                        "createdOn" to com.google.firebase.Timestamp.now()
                    )

                    // Add recipe data to Firestore
                    db.collection("RECIPE").add(recipeData)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "Recipe submitted with ID: ${documentReference.id}")
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            Log.e(TAG, "Error submitting recipe", e)
                            onFailure(e)
                        }
                }, { e ->
                    // Handle upload failure
                    onFailure(e)
                })
            } else {
                // If no image is provided, submit without an image URL
                val recipeData = hashMapOf(
                    "recipe_name" to recipeName,
                    "recipe_imageURL" to "", // Empty string if no image
                    "recipe_calories" to recipecal,
                    "recipe_cookTime" to cookTime,
                    "recipe_difficultyLevel" to difficulty,
                    "recipe_ingredients" to ingredients,
                    "recipe_instructions" to steps,
                    "is_favourite" to false,
                    "recipe_rating" to 0.0,
                    "createdBy" to userId,
                    "createdOn" to com.google.firebase.Timestamp.now()
                )

                // Add recipe data to Firestore
                db.collection("RECIPE").add(recipeData)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "Recipe submitted with ID: ${documentReference.id}")
                        onSuccess()
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error submitting recipe", e)
                        onFailure(e)
                    }
            }
        } else {
            Log.w(TAG, "Submission failed: Missing required fields")
            onFailure(Exception("Please fill all required fields"))
        }
    }
}