package com.teamfour.kooksy.ui.profile

import android.os.Parcelable
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
    // val createdOn: Timestamp? = null,
    val averageRating: Double = 0.0,
    val ratingCount: Int = 0,
    val totalRating: Int = 0,
    val is_favourite: Boolean = false,
    var is_rated: Boolean = false,
    var ratedBy: List<String> = emptyList(),
) : Parcelable
