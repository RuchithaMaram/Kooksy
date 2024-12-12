package com.teamfour.kooksy.utils

import com.google.firebase.firestore.QueryDocumentSnapshot
import com.teamfour.kooksy.ui.profile.Recipe

class Utils {
    companion object {
        fun parseResponseToRecipe(document: QueryDocumentSnapshot): Recipe {
            return Recipe(
                documentId = document.reference.id, // documentId should be first
                recipe_name = document.getString("recipe_name") ?: "",
                recipe_calories = document.getLong("recipe_calories")?.toInt() ?: 0,
                recipe_cookTime = document.getLong("recipe_cookTime")?.toInt() ?: 0,
                recipe_difficultyLevel = document.getString("recipe_difficultyLevel") ?: "",
                recipe_imageURL = document.getString("recipe_imageURL") ?: "",
                recipe_ingredients = document.get("recipe_ingredients") as? List<Map<String, String>> ?: emptyList(),
                recipe_instructions = document.get("recipe_instructions") as? List<String> ?: emptyList(),
               // createdOn = document.getTimestamp("createdOn"),
                averageRating = document.getDouble("averageRating") ?: 0.0,
                totalRating = document.getLong("totalRating")?.toInt() ?: 0,
                ratingCount = document.getLong("ratingCount")?.toInt() ?: 0,
                is_favourite = document.getBoolean("is_favourite") ?: false,
                ratedBy = document.get("ratedBy") as? List<String> ?: emptyList(),
                is_rated = document.getBoolean("is_rated") ?: false
            )
        }
    }
}