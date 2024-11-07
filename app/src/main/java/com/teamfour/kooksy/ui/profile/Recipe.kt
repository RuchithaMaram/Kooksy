package com.teamfour.kooksy.ui.profile

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

//Using same variable names from the Firestore
@Parcelize
data class Recipe(
    val recipe_name: String = "",
    val recipe_calories: Int = 0,
    val recipe_cookTime: Int = 0,
    val recipe_difficultyLevel: String = "",
    val recipe_imageURL: String = "",
    val recipe_ingredients: List<Map<String, String>> = emptyList(),
    val recipe_instructions: List<String> = emptyList(),
    val createdOn: Timestamp? = null,
    val recipe_rating: Number = 0.0f,
    val is_favourite: Boolean = false,
    val documentId: String = ""
) : Parcelable
