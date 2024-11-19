package com.teamfour.kooksy.utils

import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.getField
import com.teamfour.kooksy.ui.profile.Recipe

class Utils {
    companion object {
        fun parseResponseToRecipe(document: QueryDocumentSnapshot): Recipe {
            return Recipe(
                documentId = document.reference.id, // documentId should be first
                recipe_name = document.getString("recipe_name") ?: "",
                recipe_calories = document.getField<Int>("recipe_calories") ?: 0,
                recipe_cookTime = document.getField<Int>("recipe_cookTime") ?: 0,
                recipe_difficultyLevel = document.getString("recipe_difficultyLevel") ?: "",
                recipe_imageURL = document.getString("recipe_imageURL") ?: "",
                recipe_ingredients = document.get("recipe_ingredients") as? List<Map<String, String>> ?: emptyList(),
                recipe_instructions = document.get("recipe_instructions") as? List<String> ?: emptyList(),
                createdOn = document.getTimestamp("createdOn"),
                recipe_rating = document.getField<Double>("recipe_rating") ?: 0.0, // Changed Float to Double
                is_favourite = document.getBoolean("is_favourite") ?: false
            )
        }

    }
}