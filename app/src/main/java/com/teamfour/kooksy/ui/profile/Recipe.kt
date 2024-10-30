package com.teamfour.kooksy.ui.profile
//Using same variable nas from the Firestore
data class Recipe(
    val recipe_name: String = "",
    val recipe_calories: Int = 0,
    val recipe_cookTime: Int = 0,
    val recipe_difficultyLevel: String = "",
    val recipe_imageURL: String = "",
    val recipe_ingredients: List<Map<String, String>> = emptyList(),
    val recipe_instructions: List<String> = emptyList(),
    val createdOn: com.google.firebase.Timestamp? = null,  // Use Timestamp from Firestore
    val recipe_rating: Double = 0.0
)
