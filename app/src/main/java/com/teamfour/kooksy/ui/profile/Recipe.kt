package com.teamfour.kooksy.ui.profile

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

//Using same variable names from the Firestore
@Parcelize
data class Recipe(
    val documentId: String = "", // Add this property
    val recipe_name: String = "",
    val recipe_calories: Int = 0,
    val recipe_cookTime: Int = 0,
    val recipe_difficultyLevel: String = "",
    val recipe_imageURL: String = "",
    val recipe_ingredients: List<Map<String, String>> = emptyList(),
    val recipe_instructions: List<String> = emptyList(),
    val createdOn: Timestamp? = null,
    val recipe_rating: Double = 0.0,
    val is_favourite: Boolean = false
) : Parcelable
